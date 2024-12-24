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
    public void appendMissionUnlockedNotiHistory(List<String> receiverFamilyMemberIds) {
        receiverFamilyMemberIds.forEach(receiverFamilyMemberId -> createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        "우리 가족 모두가 생존신고를 완료했어요!",
                        "이제 미션 사진을 업로드할 수 있어요",
                        null, // TODO : 메인화면 딥링크 주소 필요
                        null,
                        "99999999999999999999999999",
                        receiverFamilyMemberId
                ))
        );
    }

    @Transactional
    public void appendCommentNotiHistory(String senderMemberName, String comment, String senderMemberId, String receiverMemberId, String aosDeepLink, String iosDeepLink) {
        createUserNotificationHistory(
                new CreateUserNotificationHistoryDTO(
                        String.format("%s님이 내 생존신고에 댓글을 달았어요", senderMemberName),
                        comment,
                        aosDeepLink,
                        iosDeepLink,
                        senderMemberId,
                        receiverMemberId
                ));
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
