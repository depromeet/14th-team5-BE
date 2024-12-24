package com.oing.service;

import com.oing.domain.ProfileStyle;
import com.oing.domain.UserNotificationHistory;
import com.oing.dto.response.NotificationResponse;
import com.oing.repository.UserNotificationHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserNotificationHistoryServiceTest {

    @InjectMocks
    private UserNotificationHistoryService userNotificationHistoryService;

    @Mock
    private UserNotificationHistoryRepository userNotificationHistoryRepository;

    @Mock
    private MemberBridge memberBridge;

    @Test
    void 최근_유저_알림_이력_조회정상_테스트() {
        // given
        String memberId = "member1";
        LocalDateTime oneMonthAgo = LocalDate.now().minusMonths(1).atStartOfDay();
        UserNotificationHistory notificationHistory = UserNotificationHistory.builder()
                .title("title")
                .content("content")
                .aosDeepLink("aosDeepLink")
                .iosDeepLink("iosDeepLink")
                .senderMemberId("sender1")
                .receiverMemberId("receiver1")
                .build();
        notificationHistory.setCreatedAt(LocalDateTime.now());

        when(userNotificationHistoryRepository.findByReceiverMemberIdAndCreatedAtAfter(memberId, oneMonthAgo))
                .thenReturn(List.of(notificationHistory));
        when(memberBridge.isBirthDayMember("sender1")).thenReturn(false);
        when(memberBridge.getMemberProfileImgUrlByMemberId("sender1")).thenReturn("http://example.com/profile.jpg");

        // when
        List<NotificationResponse> results = userNotificationHistoryService.getRecentUserNotifications(memberId);
        NotificationResponse result = results.get(0);

        // then
        assertEquals(1, results.size());
        assertEquals(ProfileStyle.NONE, result.sencerProfileStyle());
        assertEquals("http://example.com/profile.jpg", result.senderProfileImageUrl());
        assertEquals("title", result.title());
        assertEquals("content", result.content());
        assertEquals("aosDeepLink", result.aosDeepLink());
        assertEquals("iosDeepLink", result.iosDeepLink());
    }

    @Test
    void 최근_유저_알림_이력_조회정상_테스트_생일임() {
        // given
        String memberId = "member1";
        LocalDateTime oneMonthAgo = LocalDate.now().minusMonths(1).atStartOfDay();
        UserNotificationHistory notificationHistory = UserNotificationHistory.builder()
                .title("title")
                .content("content")
                .aosDeepLink("aosDeepLink")
                .iosDeepLink("iosDeepLink")
                .senderMemberId("sender1")
                .receiverMemberId("receiver1")
                .build();
        notificationHistory.setCreatedAt(LocalDateTime.now());

        when(userNotificationHistoryRepository.findByReceiverMemberIdAndCreatedAtAfter(memberId, oneMonthAgo))
                .thenReturn(List.of(notificationHistory));
        when(memberBridge.isBirthDayMember("sender1")).thenReturn(true);
        when(memberBridge.getMemberProfileImgUrlByMemberId("sender1")).thenReturn("http://example.com/profile.jpg");

        // when
        List<NotificationResponse> results = userNotificationHistoryService.getRecentUserNotifications(memberId);
        NotificationResponse result = results.get(0);

        // then
        assertEquals(1, results.size());
        assertEquals(ProfileStyle.BIRTHDAY, result.sencerProfileStyle());
        assertEquals("http://example.com/profile.jpg", result.senderProfileImageUrl());
        assertEquals("title", result.title());
        assertEquals("content", result.content());
        assertEquals("aosDeepLink", result.aosDeepLink());
        assertEquals("iosDeepLink", result.iosDeepLink());
    }

    @Test
    void 최근_유저_알림_이력_조회정상_테스트_조회결과없음() {
        // given
        String memberId = "member1";
        LocalDateTime oneMonthAgo = LocalDate.now().minusMonths(1).atStartOfDay();

        when(userNotificationHistoryRepository.findByReceiverMemberIdAndCreatedAtAfter(memberId, oneMonthAgo))
                .thenReturn(Collections.emptyList());

        // when
        List<NotificationResponse> result = userNotificationHistoryService.getRecentUserNotifications(memberId);

        // then
        assertTrue(result.isEmpty());
    }
}
