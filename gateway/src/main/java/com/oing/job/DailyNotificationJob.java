package com.oing.job;

import com.google.common.collect.Lists;
import com.google.firebase.messaging.MulticastMessage;
import com.oing.domain.BulkNotificationCompletedEvent;
import com.oing.domain.ErrorReportDTO;
import com.oing.domain.Member;
import com.oing.service.FCMNotificationService;
import com.oing.service.MemberDeviceService;
import com.oing.service.MemberPostService;
import com.oing.service.MemberService;
import com.oing.util.FCMNotificationUtil;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2/2/24
 * Time: 4:16 AM
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DailyNotificationJob {
    private final ApplicationEventPublisher eventPublisher;
    private final FCMNotificationService fcmNotificationService;

    private final MemberService memberService;
    private final MemberDeviceService memberDeviceService;
    private final MemberPostService memberPostService;

    @Scheduled(cron = "0 0 12 * * *", zone = "Asia/Seoul") // 12:00 PM
    @SchedulerLock(name = "DailyPreUploadNotificationSchedule", lockAtMostFor = "PT30S", lockAtLeastFor = "PT30S")
    public void sendDailyUploadNotification() {
        long start = System.currentTimeMillis();
        log.info("⏰ [DailyNotificationJob] 오늘 업로드 알림 전송 시작");

        try {
            HashSet<String> targetFcmTokens = new HashSet<>();
            List<Member> members = memberService.findAllActiveMembers();
            for (Member member : members) {
                targetFcmTokens.addAll(memberDeviceService.getFcmTokensByMemberId(member.getId()));
            }

            Lists.partition(targetFcmTokens.stream().toList(), 500).forEach(partitionedList -> {
                MulticastMessage multicastMessage = MulticastMessage.builder()
                        .setNotification(
                                FCMNotificationUtil.buildNotification("삐삐", "지금 바로 가족에게 일상 공유를 해볼까요?")
                        )
                        .addAllTokens(partitionedList)
                        .setApnsConfig(FCMNotificationUtil.buildApnsConfig())
                        .setAndroidConfig(FCMNotificationUtil.buildAndroidConfig())
                        .build();
                fcmNotificationService.sendMulticastMessage(multicastMessage);
            });
            log.info("[DailyNotificationJob] 오늘 업로드 알림 전송 완료. (총 {}명, {}토큰) 소요시간 : {}ms",
                    members.size(),
                    targetFcmTokens.size(),
                    System.currentTimeMillis() - start);
            Sentry.captureMessage("⏰ [DailyNotificationJob] 오늘 업로드 알림 전송 완료. (총 {"+members.size()+"}명, {"+targetFcmTokens.size()+"}토큰) 소요시간 : {"+(System.currentTimeMillis() - start)+"}ms", SentryLevel.INFO);

            eventPublisher.publishEvent(
                    new BulkNotificationCompletedEvent(
                            "오늘 업로드 알림 전송 완료", targetFcmTokens.size(), members.size(),
                            System.currentTimeMillis() - start
                    )
            );
        } catch(Exception exception) {
            eventPublisher.publishEvent(new ErrorReportDTO(exception.getMessage(), getStackTraceAsString(exception)));
        }
    }

    @Scheduled(cron = "0 30 23 * * *", zone = "Asia/Seoul") // 11:30 PM
    @SchedulerLock(name = "DailyPostUploadNotificationSchedule", lockAtMostFor = "PT30S", lockAtLeastFor = "PT30S")
    public void sendDailyRemainingNotification() {
        long start = System.currentTimeMillis();
        log.info("[DailyNotificationJob] 오늘 미 업로드 사용자 대상 알림 전송 시작");

        try {
            LocalDate today = LocalDate.now();
            List<Member> allMembers = memberService.findAllActiveMembers();
            HashSet<String> targetFcmTokens = new HashSet<>();
            HashSet<String> postedMemberIds = new HashSet<>(memberPostService.getMemberIdsPostedToday(today));
            allMembers.stream()
                    .filter(member -> !postedMemberIds.contains(member.getId())) //오늘 업로드한 사람이 아닌 사람들은
                    .forEach(member -> targetFcmTokens.addAll(memberDeviceService.getFcmTokensByMemberId(member.getId())));

            Lists.partition(targetFcmTokens.stream().toList(), 500).forEach(partitionedList -> {
                MulticastMessage multicastMessage = MulticastMessage.builder()
                        .setNotification(
                                FCMNotificationUtil.buildNotification("삐삐", "사진을 공유할 수 있는 시간이 얼마 남지 않았어요.")
                        )
                        .addAllTokens(partitionedList)
                        .setApnsConfig(FCMNotificationUtil.buildApnsConfig())
                        .setAndroidConfig(FCMNotificationUtil.buildAndroidConfig())
                        .build();
                fcmNotificationService.sendMulticastMessage(multicastMessage);
            });
            log.info("[DailyNotificationJob] 오늘 미 업로드 사용자 대상 알림 전송 완료. (총 {}명, {}토큰) 소요시간 : {}ms",
                    allMembers.size() - postedMemberIds.size(),
                    targetFcmTokens.size(),
                    System.currentTimeMillis() - start);
            Sentry.captureMessage("⏰ [DailyNotificationJob] 오늘 미 업로드 사용자 대상 알림 전송 완료. (총 {"+(allMembers.size() - postedMemberIds.size())+"}명, {"+targetFcmTokens.size()+"}토큰) 소요시간 : {"+(System.currentTimeMillis() - start)+"}ms", SentryLevel.INFO);

            eventPublisher.publishEvent(
                    new BulkNotificationCompletedEvent(
                            "오늘 미업로드자 업로드 알림 전송 완료", targetFcmTokens.size(),
                            allMembers.size() - postedMemberIds.size(),
                            System.currentTimeMillis() - start
                    )
            );
        } catch(Exception exception) {
            eventPublisher.publishEvent(new ErrorReportDTO(exception.getMessage(), getStackTraceAsString(exception)));
        }
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
