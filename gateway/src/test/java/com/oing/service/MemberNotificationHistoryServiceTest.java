package com.oing.service;

import com.oing.domain.MemberNotificationHistory;
import com.oing.domain.ProfileStyle;
import com.oing.dto.response.NotificationResponse;
import com.oing.repository.MemberNotificationHistoryRepository;
import com.oing.util.IdentityGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberNotificationHistoryServiceTest {

    @InjectMocks
    private MemberNotificationHistoryService memberNotificationHistoryService;

    @Mock
    private MemberNotificationHistoryRepository memberNotificationHistoryRepository;

    @Mock
    private MemberBridge memberBridge;

    @Mock
    private IdentityGenerator identityGenerator;

    @Test
    void 미션해금_유저_알림_이력_적재_테스트_정상수행여부 () {
        // given
        List<String> receiverFamilyMemberIds = List.of("receiver1", "receiver2");

        // when
        List<MemberNotificationHistory> result = memberNotificationHistoryService.appendMissionUnlockedNotiHistory(receiverFamilyMemberIds);

        // then
        assertEquals(2, result.size());
        verify(memberNotificationHistoryRepository, times(2)).save(any(MemberNotificationHistory.class));
    }

    @Test
    void 미션해금_유저_알림_이력_적재_테스트_데이터정합() {
        // given
        List<String> receiverFamilyMemberIds = List.of("receiver1");

        // when
        memberNotificationHistoryService.appendMissionUnlockedNotiHistory(receiverFamilyMemberIds);
        ArgumentCaptor<MemberNotificationHistory> captor = ArgumentCaptor.forClass(MemberNotificationHistory.class);
        verify(memberNotificationHistoryRepository).save(captor.capture());
        MemberNotificationHistory savedNotification = captor.getValue();

        // then
        assertEquals("우리 가족 모두가 생존신고를 완료했어요!", savedNotification.getTitle());
        assertEquals("이제 미션 사진을 업로드할 수 있어요", savedNotification.getContent());
        assertEquals("main?openMission=true", savedNotification.getAosDeepLink());
        assertEquals("main?openMission=true", savedNotification.getIosDeepLink());
        assertEquals("99999999999999999999999999", savedNotification.getSenderMemberId()); // 99999999999999999999999999 : SYSTEM 계정
        assertEquals("receiver1", savedNotification.getReceiverMemberId());
    }

    @Test
    void 미션해금_유저_알림_이력_적재_수신자가없을경우() {
        // given
        List<String> receiverFamilyMemberIds = Collections.emptyList();

        // when
        List<MemberNotificationHistory> result = memberNotificationHistoryService.appendMissionUnlockedNotiHistory(receiverFamilyMemberIds);

        // then
        assertTrue(result.isEmpty());
        verify(memberNotificationHistoryRepository, never()).save(any(MemberNotificationHistory.class));
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
        memberNotificationHistoryService.appendCommentNotiHistory(senderName, comment, senderMemberId, receiverMemberId, aosDeepLink, iosDeepLink);

        ArgumentCaptor<MemberNotificationHistory> captor = ArgumentCaptor.forClass(MemberNotificationHistory.class);
        verify(memberNotificationHistoryRepository).save(captor.capture());
        MemberNotificationHistory savedNotification = captor.getValue();

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
        List<MemberNotificationHistory> result = memberNotificationHistoryService.appendNextWeekBirthdayNotiHistory(senderName, senderMemberId, receiverMemberIds);

        // then
        assertEquals(2, result.size());
        verify(memberNotificationHistoryRepository, times(2)).save(any(MemberNotificationHistory.class));
    }

    @Test
    void 다음주_생일자_알림_이력_적재_테스트_데이터정합성() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        String receiverMemberId = "receiver1";

        // when
        memberNotificationHistoryService.appendNextWeekBirthdayNotiHistory(senderName, senderMemberId, List.of(receiverMemberId));

        ArgumentCaptor<MemberNotificationHistory> captor = ArgumentCaptor.forClass(MemberNotificationHistory.class);
        verify(memberNotificationHistoryRepository).save(captor.capture());
        MemberNotificationHistory savedNotification = captor.getValue();

        // then
        assertEquals(String.format("일주일 뒤 %s님의 생일이에요!", senderName), savedNotification.getTitle());
        assertEquals("잊고 계신건 아니겠죠??", savedNotification.getContent());
        assertEquals("main/profile/" + senderMemberId, savedNotification.getAosDeepLink());
        assertEquals("main/profile/" + senderMemberId, savedNotification.getIosDeepLink());
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
        List<MemberNotificationHistory> result = memberNotificationHistoryService.appendNextWeekBirthdayNotiHistory(senderName, senderMemberId, receiverMemberIds);

        // then
        assertTrue(result.isEmpty());
        verify(memberNotificationHistoryRepository, never()).save(any(MemberNotificationHistory.class));
    }

    @Test
    void 내일_생일자_알림_이력_적재_테스트_정상수행여부() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        List<String> receiverMemberIds = List.of("receiver1", "receiver2");

        // when
        List<MemberNotificationHistory> result = memberNotificationHistoryService.appendTomorrowBirthdayNotiHistory(senderName, senderMemberId, receiverMemberIds);

        // then
        assertEquals(2, result.size());
        verify(memberNotificationHistoryRepository, times(2)).save(any(MemberNotificationHistory.class));
    }

    @Test
    void 내_생일자_알림_이력_적재_테스트_데이터정합성() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        String receiverMemberId = "receiver1";

        // when
        memberNotificationHistoryService.appendTomorrowBirthdayNotiHistory(senderName, senderMemberId, List.of(receiverMemberId));

        ArgumentCaptor<MemberNotificationHistory> captor = ArgumentCaptor.forClass(MemberNotificationHistory.class);
        verify(memberNotificationHistoryRepository).save(captor.capture());
        MemberNotificationHistory savedNotification = captor.getValue();

        // then
        assertEquals(String.format("내일 %s님의 생일이에요!", senderName), savedNotification.getTitle());
        assertEquals("잊고 계신건 아니겠죠??", savedNotification.getContent());
        assertEquals("main/profile/" + senderMemberId, savedNotification.getAosDeepLink());
        assertEquals("main/profile/" + senderMemberId, savedNotification.getIosDeepLink());
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
        List<MemberNotificationHistory> result = memberNotificationHistoryService.appendTomorrowBirthdayNotiHistory(senderName, senderMemberId, receiverMemberIds);

        // then
        assertTrue(result.isEmpty());
        verify(memberNotificationHistoryRepository, never()).save(any(MemberNotificationHistory.class));
    }

    @Test
    void 오늘_생일자_알림_이력_적재_테스트_정상수행여부() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        List<String> receiverMemberIds = List.of("receiver1", "receiver2");

        // when
        List<MemberNotificationHistory> result = memberNotificationHistoryService.appendTodayBirthdayNotiHistory(senderName, senderMemberId, receiverMemberIds);

        // then
        assertEquals(2, result.size());
        verify(memberNotificationHistoryRepository, times(2)).save(any(MemberNotificationHistory.class));
    }

    @Test
    void 오늘_생일자_알림_이력_적재_테스트_데이터정합성() {
        // given
        String senderName = "sender";
        String senderMemberId = "sender1";
        String receiverMemberId = "receiver1";

        // when
        memberNotificationHistoryService.appendTodayBirthdayNotiHistory(senderName, senderMemberId, List.of(receiverMemberId));

        ArgumentCaptor<MemberNotificationHistory> captor = ArgumentCaptor.forClass(MemberNotificationHistory.class);
        verify(memberNotificationHistoryRepository).save(captor.capture());
        MemberNotificationHistory savedNotification = captor.getValue();

        // then
        assertEquals(String.format("오늘 %s님의 생일이에요!", senderName), savedNotification.getTitle());
        assertEquals(String.format("모두 %s님의 생일을 축하해주세요!", senderName), savedNotification.getContent());
        assertEquals("main/profile/" + senderMemberId, savedNotification.getAosDeepLink());
        assertEquals("main/profile/" + senderMemberId, savedNotification.getIosDeepLink());
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
        List<MemberNotificationHistory> result = memberNotificationHistoryService.appendTodayBirthdayNotiHistory(senderName, senderMemberId, receiverMemberIds);

        // then
        assertTrue(result.isEmpty());
        verify(memberNotificationHistoryRepository, never()).save(any(MemberNotificationHistory.class));
    }

    @Test
    void 신버전_출시_알림_이력_적재_테스트_정상수행여부() {
        // given
        List<String> allActiveMemberIds = List.of("activeMember1", "activeMember2");

        // when
        List<MemberNotificationHistory> result = memberNotificationHistoryService.appendAppNewVersionReleasedNotiHistory(allActiveMemberIds);

        // then
        assertEquals(2, result.size());
        verify(memberNotificationHistoryRepository, times(2)).save(any(MemberNotificationHistory.class));
    }

    @Test
    void 신버전_출시_알림_이력_적재_테스트_데이터정합성() {
        List<String> allActiveMemberIds = List.of("activeMember1");

        List<MemberNotificationHistory> result = memberNotificationHistoryService.appendAppNewVersionReleasedNotiHistory(allActiveMemberIds);

        ArgumentCaptor<MemberNotificationHistory> captor = ArgumentCaptor.forClass(MemberNotificationHistory.class);
        verify(memberNotificationHistoryRepository).save(captor.capture());
        MemberNotificationHistory savedNotification = captor.getValue();

        assertEquals("삐삐의 새로운 버전이 출시되었어요!", savedNotification.getTitle());
        assertEquals("지금 바로 업데이트 해주세요!", savedNotification.getContent());
        assertEquals("store/bibbi", savedNotification.getAosDeepLink());
        assertEquals("store/bibbi", savedNotification.getIosDeepLink());
        assertEquals("99999999999999999999999998", savedNotification.getSenderMemberId()); // 99999999999999999999999998 : NOTICE 계정
        assertEquals("activeMember1", savedNotification.getReceiverMemberId());
    }

    @Test
    void 신버전_출시_알림_이력_적재_테스트_수신자없을경우() {
        // given
        List<String> allActiveMemberIds = Collections.emptyList();

        // when
        List<MemberNotificationHistory> result = memberNotificationHistoryService.appendAppNewVersionReleasedNotiHistory(allActiveMemberIds);

        // then
        assertTrue(result.isEmpty());
        verify(memberNotificationHistoryRepository, never()).save(any(MemberNotificationHistory.class));
    }

    @Test
    void 최근_유저_알림_이력_조회정상_테스트() {
        // given
        String memberId = "member1";
        LocalDateTime oneMonthAgo = LocalDate.now().minusMonths(1).atStartOfDay();
        MemberNotificationHistory notificationHistory = com.oing.domain.MemberNotificationHistory.builder()
                .title("title")
                .content("content")
                .aosDeepLink("aosDeepLink")
                .iosDeepLink("iosDeepLink")
                .senderMemberId("sender1")
                .receiverMemberId(memberId)  // receiverMemberId가 memberId와 같아야 조회가 맞음
                .build();
        notificationHistory.setCreatedAt(LocalDateTime.now());

        Pageable pageable = Pageable.unpaged(); // 혹은 원하는 Pageable 객체 생성
        Page<MemberNotificationHistory> page = new PageImpl<>(List.of(notificationHistory), pageable, 1);

        when(memberNotificationHistoryRepository.findByReceiverMemberIdAndCreatedAtAfterOrderByCreatedAtDesc(
                eq(memberId), eq(oneMonthAgo), any(Pageable.class)))
                .thenReturn(page);

        when(memberBridge.isBirthDayMember("sender1")).thenReturn(false);
        when(memberBridge.getMemberProfileImgUrlByMemberId("sender1")).thenReturn("http://example.com/profile.jpg");

        // when
        List<NotificationResponse> results = memberNotificationHistoryService.getRecentUserNotifications(memberId, 0, 10);
        NotificationResponse result = results.get(0);

        // then
        assertEquals(1, results.size());
        assertEquals(ProfileStyle.NONE, result.senderProfileStyle());
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
        MemberNotificationHistory notificationHistory = com.oing.domain.MemberNotificationHistory.builder()
                .title("title")
                .content("content")
                .aosDeepLink("aosDeepLink")
                .iosDeepLink("iosDeepLink")
                .senderMemberId("sender1")
                .receiverMemberId(memberId)  // receiverId도 맞춰주세요
                .build();
        notificationHistory.setCreatedAt(LocalDateTime.now());

        Pageable pageable = Pageable.unpaged();
        Page<MemberNotificationHistory> page = new PageImpl<>(List.of(notificationHistory), pageable, 1);

        when(memberNotificationHistoryRepository.findByReceiverMemberIdAndCreatedAtAfterOrderByCreatedAtDesc(
                eq(memberId), eq(oneMonthAgo), any(Pageable.class)))
                .thenReturn(page);

        when(memberBridge.isBirthDayMember("sender1")).thenReturn(true);
        when(memberBridge.getMemberProfileImgUrlByMemberId("sender1")).thenReturn("http://example.com/profile.jpg");

        // when
        List<NotificationResponse> results = memberNotificationHistoryService.getRecentUserNotifications(memberId, 0, 10);
        NotificationResponse result = results.get(0);

        // then
        assertEquals(1, results.size());
        assertEquals(ProfileStyle.BIRTHDAY, result.senderProfileStyle());
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

        Pageable pageable = Pageable.unpaged();
        Page<MemberNotificationHistory> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(memberNotificationHistoryRepository.findByReceiverMemberIdAndCreatedAtAfterOrderByCreatedAtDesc(
                eq(memberId), eq(oneMonthAgo), any(Pageable.class)))
                .thenReturn(emptyPage);

        // when
        List<NotificationResponse> result = memberNotificationHistoryService.getRecentUserNotifications(memberId, 0, 10);

        // then
        assertTrue(result.isEmpty());
    }
}
