package com.oing.service;

import com.oing.domain.ProfileStyle;
import com.oing.domain.UserNotificationHistory;
import com.oing.dto.response.NotificationResponse;
import com.oing.repository.UserNotificationHistoryRepository;
import com.oing.util.IdentityGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserNotificationHistoryServiceTest {

    @InjectMocks
    private UserNotificationHistoryService userNotificationHistoryService;

    @Mock
    private UserNotificationHistoryRepository userNotificationHistoryRepository;

    @Mock
    private MemberBridge memberBridge;

    @Mock
    private IdentityGenerator identityGenerator;

    @Test
    void 미션해금_유저_알림_이력_적재_테스트_정상수행여부 () {
        // given
        List<String> receiverFamilyMemberIds = List.of("receiver1", "receiver2");

        // when
        List<UserNotificationHistory> result = userNotificationHistoryService.appendMissionUnlockedNotiHistory(receiverFamilyMemberIds);

        // then
        assertEquals(2, result.size());
        verify(userNotificationHistoryRepository, times(2)).save(any(UserNotificationHistory.class));
    }

    @Test
    void 미션해금_유저_알림_이력_적재_테스트_데이터정합() {
        // given
        List<String> receiverFamilyMemberIds = List.of("receiver1");

        // when
        userNotificationHistoryService.appendMissionUnlockedNotiHistory(receiverFamilyMemberIds);
        ArgumentCaptor<UserNotificationHistory> captor = ArgumentCaptor.forClass(UserNotificationHistory.class);
        verify(userNotificationHistoryRepository).save(captor.capture());
        UserNotificationHistory savedNotification = captor.getValue();

        // then
        assertEquals("우리 가족 모두가 생존신고를 완료했어요!", savedNotification.getTitle());
        assertEquals("이제 미션 사진을 업로드할 수 있어요", savedNotification.getContent());
        assertNull(savedNotification.getAosDeepLink()); // TODO : 메인화면 딥링크 주소 필요
        assertNull(savedNotification.getIosDeepLink());
        assertEquals("99999999999999999999999999", savedNotification.getSenderMemberId()); // 99999999999999999999999999 : SYSTEM 계정
        assertEquals("receiver1", savedNotification.getReceiverMemberId());
    }

    @Test
    void 미션해금_유저_알림_이력_적재_수신자가없을경우() {
        // given
        List<String> receiverFamilyMemberIds = Collections.emptyList();

        // when
        List<UserNotificationHistory> result = userNotificationHistoryService.appendMissionUnlockedNotiHistory(receiverFamilyMemberIds);

        // then
        assertTrue(result.isEmpty());
        verify(userNotificationHistoryRepository, never()).save(any(UserNotificationHistory.class));
    }

    @Test
    void 댓글_유저_알림_이력_적재_테스트() {
        // given
        String senderName = "sender";
        String comment = "This is a comment";
        String senderMemberId = "sender1";
        String receiverMemberId = "receiver1";
        String aosDeepLink = "post/view/123?openComment=true";
        String iosDeepLink = "post/view/123?openComment=true&dateOfPost=2024-12-24";

        // when
        userNotificationHistoryService.appendCommentNotiHistory(senderName, comment, senderMemberId, receiverMemberId, aosDeepLink, iosDeepLink);

        ArgumentCaptor<UserNotificationHistory> captor = ArgumentCaptor.forClass(UserNotificationHistory.class);
        verify(userNotificationHistoryRepository).save(captor.capture());
        UserNotificationHistory savedNotification = captor.getValue();

        // then
        assertEquals(String.format("%s님이 내 생존신고에 댓글을 달았어요", senderName), savedNotification.getTitle());
        assertEquals(comment, savedNotification.getContent());
        assertEquals(aosDeepLink, savedNotification.getAosDeepLink());
        assertEquals(iosDeepLink, savedNotification.getIosDeepLink());
        assertEquals(senderMemberId, savedNotification.getSenderMemberId());
        assertEquals(receiverMemberId, savedNotification.getReceiverMemberId());
    }

    @Test
    void 다음주_생일자_알림_이력_적재_테스트_정상수행여부() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        List<String> receiverMemberIds = List.of("receiver1", "receiver2");

        // when
        List<UserNotificationHistory> result = userNotificationHistoryService.appendNextWeekBirthdayNotiHistory(senderName, senderMemberId, receiverMemberIds);

        // then
        assertEquals(2, result.size());
        verify(userNotificationHistoryRepository, times(2)).save(any(UserNotificationHistory.class));
    }

    @Test
    void 다음주_생일자_알림_이력_적재_테스트_데이터정합성() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        String receiverMemberId = "receiver1";

        // when
        userNotificationHistoryService.appendNextWeekBirthdayNotiHistory(senderName, senderMemberId, List.of(receiverMemberId));

        ArgumentCaptor<UserNotificationHistory> captor = ArgumentCaptor.forClass(UserNotificationHistory.class);
        verify(userNotificationHistoryRepository).save(captor.capture());
        UserNotificationHistory savedNotification = captor.getValue();

        // then
        assertEquals(String.format("일주일 뒤 %s님의 생일이에요!", senderName), savedNotification.getTitle());
        assertEquals("잊고 계신건 아니겠죠??", savedNotification.getContent());
        assertNull(savedNotification.getAosDeepLink()); // TODO : 메인화면 딥링크 주소 필요
        assertNull(savedNotification.getIosDeepLink());
        assertEquals(senderMemberId, savedNotification.getSenderMemberId());
        assertEquals(receiverMemberId, savedNotification.getReceiverMemberId());
    }

    @Test
    void 다음주_생일자_알림_이력_적재_테스트_수신자없을경우() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        List<String> receiverMemberIds = Collections.emptyList();

        // when
        List<UserNotificationHistory> result = userNotificationHistoryService.appendNextWeekBirthdayNotiHistory(senderName, senderMemberId, receiverMemberIds);

        // then
        assertTrue(result.isEmpty());
        verify(userNotificationHistoryRepository, never()).save(any(UserNotificationHistory.class));
    }

    @Test
    void 내일_생일자_알림_이력_적재_테스트_정상수행여부() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        List<String> receiverMemberIds = List.of("receiver1", "receiver2");

        // when
        List<UserNotificationHistory> result = userNotificationHistoryService.appendTomorrowBirthdayNotiHistory(senderName, senderMemberId, receiverMemberIds);

        // then
        assertEquals(2, result.size());
        verify(userNotificationHistoryRepository, times(2)).save(any(UserNotificationHistory.class));
    }

    @Test
    void 내_생일자_알림_이력_적재_테스트_데이터정합성() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        String receiverMemberId = "receiver1";

        // when
        userNotificationHistoryService.appendTomorrowBirthdayNotiHistory(senderName, senderMemberId, List.of(receiverMemberId));

        ArgumentCaptor<UserNotificationHistory> captor = ArgumentCaptor.forClass(UserNotificationHistory.class);
        verify(userNotificationHistoryRepository).save(captor.capture());
        UserNotificationHistory savedNotification = captor.getValue();

        // then
        assertEquals(String.format("내일 %s님의 생일이에요!", senderName), savedNotification.getTitle());
        assertEquals("잊고 계신건 아니겠죠??", savedNotification.getContent());
        assertNull(savedNotification.getAosDeepLink()); // TODO : 메인화면 딥링크 주소 필요
        assertNull(savedNotification.getIosDeepLink());
        assertEquals(senderMemberId, savedNotification.getSenderMemberId());
        assertEquals(receiverMemberId, savedNotification.getReceiverMemberId());
    }

    @Test
    void 내일_생일자_알림_이력_적재_테스트_수신자없을경우() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        List<String> receiverMemberIds = Collections.emptyList();

        // when
        List<UserNotificationHistory> result = userNotificationHistoryService.appendTomorrowBirthdayNotiHistory(senderName, senderMemberId, receiverMemberIds);

        // then
        assertTrue(result.isEmpty());
        verify(userNotificationHistoryRepository, never()).save(any(UserNotificationHistory.class));
    }

    @Test
    void 오늘_생일자_알림_이력_적재_테스트_정상수행여부() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        List<String> receiverMemberIds = List.of("receiver1", "receiver2");

        // when
        List<UserNotificationHistory> result = userNotificationHistoryService.appendTodayBirthdayNotiHistory(senderName, senderMemberId, receiverMemberIds);

        // then
        assertEquals(2, result.size());
        verify(userNotificationHistoryRepository, times(2)).save(any(UserNotificationHistory.class));
    }

    @Test
    void 오늘_생일자_알림_이력_적재_테스트_데이터정합성() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        String receiverMemberId = "receiver1";

        // when
        userNotificationHistoryService.appendTodayBirthdayNotiHistory(senderName, senderMemberId, List.of(receiverMemberId));

        ArgumentCaptor<UserNotificationHistory> captor = ArgumentCaptor.forClass(UserNotificationHistory.class);
        verify(userNotificationHistoryRepository).save(captor.capture());
        UserNotificationHistory savedNotification = captor.getValue();

        // then
        assertEquals(String.format("오늘 %s님의 생일이에요!", senderName), savedNotification.getTitle());
        assertEquals(String.format("모두 %s님의 생일을 축하해주세요!", senderName), savedNotification.getContent());
        assertNull(savedNotification.getAosDeepLink()); // TODO : 메인화면 딥링크 주소 필요
        assertNull(savedNotification.getIosDeepLink());
        assertEquals(senderMemberId, savedNotification.getSenderMemberId());
        assertEquals(receiverMemberId, savedNotification.getReceiverMemberId());
    }

    @Test
    void 오늘_생일자_알림_이력_적재_테스트_수신자없을경우() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        List<String> receiverMemberIds = Collections.emptyList();

        // when
        List<UserNotificationHistory> result = userNotificationHistoryService.appendTodayBirthdayNotiHistory(senderName, senderMemberId, receiverMemberIds);

        // then
        assertTrue(result.isEmpty());
        verify(userNotificationHistoryRepository, never()).save(any(UserNotificationHistory.class));
    }

    @Test
    void 신버전_출시_알림_이력_적재_테스트_정상수행여부() {
        // given
        List<String> allActiveMemberIds = List.of("activeMember1", "activeMember2");

        // when
        List<UserNotificationHistory> result = userNotificationHistoryService.appendAppNewVersionReleasedNotiHistory(allActiveMemberIds);

        // then
        assertEquals(2, result.size());
        verify(userNotificationHistoryRepository, times(2)).save(any(UserNotificationHistory.class));
    }

    @Test
    void 신버전_출시_알림_이력_적재_테스트_데이터정합성() {
        List<String> allActiveMemberIds = List.of("activeMember1");

        List<UserNotificationHistory> result = userNotificationHistoryService.appendAppNewVersionReleasedNotiHistory(allActiveMemberIds);

        ArgumentCaptor<UserNotificationHistory> captor = ArgumentCaptor.forClass(UserNotificationHistory.class);
        verify(userNotificationHistoryRepository).save(captor.capture());
        UserNotificationHistory savedNotification = captor.getValue();

        assertEquals("삐삐의 새로운 버전이 출시되었어요!", savedNotification.getTitle());
        assertEquals("지금 바로 업데이트 해주세요!", savedNotification.getContent());
        assertNull(savedNotification.getAosDeepLink()); // TODO : 메인화면 딥링크 주소 필요
        assertNull(savedNotification.getIosDeepLink());
        assertEquals("99999999999999999999999998", savedNotification.getSenderMemberId()); // 99999999999999999999999998 : NOTICE 계정
        assertEquals("activeMember1", savedNotification.getReceiverMemberId());
    }

    @Test
    void 신버전_출시_알림_이력_적재_테스트_수신자없을경우() {
        // given
        List<String> allActiveMemberIds = Collections.emptyList();

        // when
        List<UserNotificationHistory> result = userNotificationHistoryService.appendAppNewVersionReleasedNotiHistory(allActiveMemberIds);

        // then
        assertTrue(result.isEmpty());
        verify(userNotificationHistoryRepository, never()).save(any(UserNotificationHistory.class));
    }

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
