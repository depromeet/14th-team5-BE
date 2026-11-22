package com.oing.job;

import com.google.common.collect.Lists;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.oing.domain.Member;
import com.oing.service.MemberDeviceService;
import com.oing.service.MemberService;
import com.oing.util.FCMNotificationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Profile("prod")
@RequiredArgsConstructor
public class ServiceClosureNotificationJob {
    private static final String ZONE = "Asia/Seoul";
    private static final LocalDate SEND_DATE = LocalDate.of(2026, 11, 23);
    private static final String NOTIFICATION_ID = "service-closure-2026-11-23";

    private final MemberService memberService;
    private final MemberDeviceService memberDeviceService;
    private final FirebaseMessaging firebaseMessaging;
    private final JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 0 9 23 11 *", zone = ZONE)
    @SchedulerLock(name = "ServiceClosureNotificationSchedule", lockAtMostFor = "PT1H")
    public void sendServiceClosureNotification() {
        if (!SEND_DATE.equals(LocalDate.now(ZoneId.of(ZONE)))) {
            return;
        }

        Set<String> tokens = new HashSet<>();
        for (Member member : memberService.findAllActiveMembers()) {
            tokens.addAll(memberDeviceService.getFcmTokensByMemberId(member.getId()));
        }
        tokens.removeIf(token -> token == null || token.isBlank());

        try {
            jdbcTemplate.update("""
                    INSERT INTO bulk_notification_run (notification_id, target_count)
                    VALUES (?, ?)
                    """, NOTIFICATION_ID, tokens.size());
        } catch (DuplicateKeyException exception) {
            log.warn("Skipping service closure notification because a run already exists");
            return;
        }

        log.info("Starting service closure notification for {} FCM tokens", tokens.size());
        int accepted = 0;
        int failed = 0;
        int unknown = 0;
        for (List<String> batch : Lists.partition(List.copyOf(tokens), 500)) {
            MulticastMessage message = MulticastMessage.builder()
                    .setNotification(FCMNotificationUtil.buildNotification(
                            "삐삐 서비스 종료 안내",
                            "삐삐는 11월 30일(월) 밤 12시(12월 1일 오전 0시)까지 서비스할 예정입니다."))
                    .setApnsConfig(FCMNotificationUtil.buildApnsConfig())
                    .setAndroidConfig(FCMNotificationUtil.buildAndroidConfig())
                    .addAllTokens(batch)
                    .build();

            try {
                BatchResponse response = firebaseMessaging.sendEachForMulticast(message);
                accepted += response.getSuccessCount();
                failed += response.getFailureCount();
            } catch (FirebaseMessagingException exception) {
                unknown += batch.size();
                log.error("FCM returned no per-token results for a batch of {} service closure notifications (error: {})",
                        batch.size(), exception.getMessagingErrorCode());
            }

            jdbcTemplate.update("""
                    UPDATE bulk_notification_run
                    SET accepted_count = ?, failed_count = ?, unknown_count = ?
                    WHERE notification_id = ?
                    """, accepted, failed, unknown, NOTIFICATION_ID);
        }

        jdbcTemplate.update("""
                UPDATE bulk_notification_run SET finished_at = CURRENT_TIMESTAMP
                WHERE notification_id = ?
                """, NOTIFICATION_ID);
        log.info("Service closure notification finished: target={}, accepted={}, failed={}, unknown={}",
                tokens.size(), accepted, failed, unknown);
    }
}
