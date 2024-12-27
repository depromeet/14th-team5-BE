package com.oing.service;

import com.oing.domain.ProfileStyle;
import com.oing.domain.UserNotificationHistory;
import com.oing.dto.request.CreateUserNotificationHistoryDTO;
import com.oing.dto.response.NotificationResponse;
import com.oing.repository.UserNotificationHistoryRepository;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserNotificationHistoryService {

    private final UserNotificationHistoryRepository userNotificationHistoryRepository;
    private final MemberBridge memberBridge;
    private final IdentityGenerator identityGenerator;

    @Transactional
    public List<UserNotificationHistory> appendMissionUnlockedNotiHistory(List<String> receiverFamilyMemberIds) {
        return receiverFamilyMemberIds.stream().map(receiverFamilyMemberId -> createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        "우리 가족 모두가 생존신고를 완료했어요!",
                        "이제 미션 사진을 업로드할 수 있어요",
                        null,
                        null,
                        "99999999999999999999999999", // 99999999999999999999999999 : SYSTEM 계정
                        receiverFamilyMemberId
                )) // TODO : 메인화면 딥링크 주소 필요
        ).toList();
    }

    @Transactional
    public UserNotificationHistory appendCommentNotiHistory(String senderName, String comment, String senderMemberId, String receiverMemberId, String aosDeepLink, String iosDeepLink) {
        return createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        String.format("%s님이 내 생존신고에 댓글을 달았어요", senderName),
                        comment,
                        aosDeepLink,
                        iosDeepLink,
                        senderMemberId,
                        receiverMemberId
                )
        );
    }

    @Transactional
    public List<UserNotificationHistory> appendNextWeekBirthdayNotiHistory(String senderName, String senderMemberId, List<String> receiverMemberIds) {
        return receiverMemberIds.stream().map(receiverMemberId -> createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        String.format("일주일 뒤 %s님의 생일이에요!", senderName),
                        "잊고 계신건 아니겠죠??",
                        null,
                        null,
                        senderMemberId,
                        receiverMemberId
                )) // TODO : 메인화면 딥링크 주소 필요
        ).toList();
    }

    @Transactional
    public List<UserNotificationHistory> appendTomorrowBirthdayNotiHistory(String senderName, String senderMemberId, List<String> receiverMemberIds) {
        return receiverMemberIds.stream().map(receiverMemberId -> createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        String.format("내일 %s님의 생일이에요!", senderName),
                        "잊고 계신건 아니겠죠??",
                        null,
                        null,
                        senderMemberId,
                        receiverMemberId
                )) // TODO : 메인화면 딥링크 주소 필요
        ).toList();
    }

    @Transactional
    public List<UserNotificationHistory> appendTodayBirthdayNotiHistory(String senderName, String senderMemberId, List<String> receiverMemberIds) {
        return receiverMemberIds.stream().map(receiverMemberId -> createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        String.format("오늘 %s님의 생일이에요!", senderName),
                        String.format("모두 %s님의 생일을 축하해주세요!", senderName),
                        null,
                        null,
                        senderMemberId,
                        receiverMemberId
                )) // TODO : 메인화면 딥링크 주소 필요
        ).toList();
    }

    @Transactional
    public List<UserNotificationHistory> appendAppNewVersionReleasedNotiHistory(List<String> allActiveMemberIds) {
        return allActiveMemberIds.stream().map(activeMemberId -> createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        "삐삐의 새로운 버전이 출시되었어요!",
                        "지금 바로 업데이트 해주세요!",
                        null,
                        null,
                        "99999999999999999999999998", // 99999999999999999999999998 : NOTICE 계정
                        activeMemberId
                )) // TODO : 신버전 다운로드 딥링크 주소 필요
        ).toList();
    }

    private UserNotificationHistory createUserNotificationHistory(CreateUserNotificationHistoryDTO createUserNotificationHistoryDTO) {
        return userNotificationHistoryRepository.save(
                createUserNotificationHistoryDTO.toEntity(identityGenerator.generateIdentity()));
    }

    public List<NotificationResponse> getRecentUserNotifications(String memberId) {
        // 최근 한 달간의 알림 내역 조회
        LocalDateTime oneMonthAgo = LocalDate.now().minusMonths(1).atStartOfDay();

        return userNotificationHistoryRepository.findByReceiverMemberIdAndCreatedAtAfter(memberId, oneMonthAgo)
                .stream()
                .map(userNotificationHistory -> {
                    ProfileStyle senderProfileStyle = ProfileStyle.NONE;
                    if (memberBridge.isBirthDayMember(userNotificationHistory.getSenderMemberId())) {
                        senderProfileStyle = ProfileStyle.BIRTHDAY;
                    }

                    String senderProfileImageUrl = memberBridge.getMemberProfileImgUrlByMemberId(userNotificationHistory.getSenderMemberId());

                    return NotificationResponse.of(userNotificationHistory, senderProfileImageUrl, senderProfileStyle);

                })
                .toList();
    }
}
