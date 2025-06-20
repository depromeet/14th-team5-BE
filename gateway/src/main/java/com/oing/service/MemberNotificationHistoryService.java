package com.oing.service;

import com.oing.domain.MemberNotificationHistory;
import com.oing.domain.ProfileStyle;
import com.oing.dto.request.CreateUserNotificationHistoryDTO;
import com.oing.dto.response.NotificationResponse;
import com.oing.repository.MemberNotificationHistoryRepository;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberNotificationHistoryService {

    private final MemberNotificationHistoryRepository memberNotificationHistoryRepository;
    private final MemberBridge memberBridge;
    private final IdentityGenerator identityGenerator;

    @Transactional
    public List<MemberNotificationHistory> appendMissionUnlockedNotiHistory(List<String> receiverFamilyMemberIds) {
        return receiverFamilyMemberIds.stream().map(receiverFamilyMemberId -> createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        "우리 가족 모두가 생존신고를 완료했어요!",
                        "이제 미션 사진을 업로드할 수 있어요",
                        "main?openMission=true",
                        "main?openMission=true",
                        "99999999999999999999999999", // 99999999999999999999999999 : SYSTEM 계정
                        receiverFamilyMemberId
                )) // TODO : 메인화면 딥링크 주소 필요
        ).toList();
    }

    @Transactional
    public MemberNotificationHistory appendCommentNotiHistory(String senderName, String comment, String senderMemberId, String receiverMemberId, String aosDeepLink, String iosDeepLink) {
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
    public List<MemberNotificationHistory> appendNextWeekBirthdayNotiHistory(String senderName, String senderMemberId, List<String> receiverMemberIds) {
        return receiverMemberIds.stream().map(receiverMemberId -> createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        String.format("일주일 뒤 %s님의 생일이에요!", senderName),
                        "잊고 계신건 아니겠죠??",
                        "main/profile/" + senderMemberId,
                        "main/profile/" + senderMemberId,
                        senderMemberId,
                        receiverMemberId
                )) // TODO : 메인화면 딥링크 주소 필요
        ).toList();
    }

    @Transactional
    public List<MemberNotificationHistory> appendTomorrowBirthdayNotiHistory(String senderName, String senderMemberId, List<String> receiverMemberIds) {
        return receiverMemberIds.stream().map(receiverMemberId -> createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        String.format("내일 %s님의 생일이에요!", senderName),
                        "잊고 계신건 아니겠죠??",
                        "main/profile/" + senderMemberId,
                        "main/profile/" + senderMemberId,
                        senderMemberId,
                        receiverMemberId
                )) // TODO : 메인화면 딥링크 주소 필요
        ).toList();
    }

    @Transactional
    public List<MemberNotificationHistory> appendTodayBirthdayNotiHistory(String senderName, String senderMemberId, List<String> receiverMemberIds) {
        return receiverMemberIds.stream().map(receiverMemberId -> createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        String.format("오늘 %s님의 생일이에요!", senderName),
                        String.format("모두 %s님의 생일을 축하해주세요!", senderName),
                        "main/profile/" + senderMemberId,
                        "main/profile/" + senderMemberId,
                        senderMemberId,
                        receiverMemberId
                )) // TODO : 메인화면 딥링크 주소 필요
        ).toList();
    }

    @Transactional
    public List<MemberNotificationHistory> appendAppNewVersionReleasedNotiHistory(List<String> allActiveMemberIds) {
        return allActiveMemberIds.stream().map(activeMemberId -> createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        "삐삐의 새로운 버전이 출시되었어요!",
                        "지금 바로 업데이트 해주세요!",
                        "store/bibbi",
                        "store/bibbi",
                        "99999999999999999999999998", // 99999999999999999999999998 : NOTICE 계정
                        activeMemberId
                )) // TODO : 신버전 다운로드 딥링크 주소 필요
        ).toList();
    }

    private MemberNotificationHistory createUserNotificationHistory(CreateUserNotificationHistoryDTO createUserNotificationHistoryDTO) {
        return memberNotificationHistoryRepository.save(
                createUserNotificationHistoryDTO.toEntity(identityGenerator.generateIdentity()));
    }

    public List<NotificationResponse> getRecentUserNotifications(String memberId, int page, int size) {
        // 최근 한 달간의 알림 내역 조회
        LocalDateTime oneMonthAgo = LocalDate.now().minusMonths(1).atStartOfDay();
        Pageable pageable = PageRequest.of(page, size);

        return memberNotificationHistoryRepository
                .findByReceiverMemberIdAndCreatedAtAfterOrderByCreatedAtDesc(memberId, oneMonthAgo, pageable)
                .stream()
                .map(userNotificationHistory -> {
                    String senderMemberId = userNotificationHistory.getSenderMemberId();
                    ProfileStyle senderProfileStyle = ProfileStyle.NONE;
                    if (memberBridge.isBirthDayMember(senderMemberId)) {
                        senderProfileStyle = ProfileStyle.BIRTHDAY;
                    }

                    String senderProfileImageUrl = memberBridge.getMemberProfileImgUrlByMemberId(senderMemberId);
                    return NotificationResponse.of(userNotificationHistory, senderMemberId, senderProfileImageUrl, senderProfileStyle);
                })
                .toList();
    }

    public String findLatestNotificationIdByMemberId(String memberId) {
        return memberNotificationHistoryRepository.findLatestNotificationIdByReceiverMemberId(memberId)
                .orElse(null);
    }
}
