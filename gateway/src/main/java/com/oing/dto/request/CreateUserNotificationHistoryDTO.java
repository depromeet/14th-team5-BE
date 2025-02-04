package com.oing.dto.request;

import com.oing.domain.MemberNotificationHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "유저 알림 이력 생성 요청")
public record CreateUserNotificationHistoryDTO(
        @NotBlank
        @Schema(description = "알림 제목", example = "엄마님이 내 생존신고에 댓글을 달았어요")
        String title,

        @NotBlank
        @Schema(description = "알림 내용", example = "우와~ 맛있겠다!!")
        String content,

        @Schema(description = "AOS 딥링크", example = "post/view/123?openComment=true")
        String aosDeepLink,

        @Schema(description = "iOS 딥링크", example = "post/view/123?openComment=true&dateOfPost=2024-12-24")
        String iosDeepLink,

        @Size(min = 26, max = 26)
        @Schema(description = "알림 전송자 유저 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String senderMemberId,

        @Size(min = 26, max = 26)
        @Schema(description = "알림 수신자 유저 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String receiverMemberId
) {
        public MemberNotificationHistory toEntity(String id) {
                return com.oing.domain.MemberNotificationHistory.builder()
                        .id(id)
                        .title(title)
                        .content(content)
                        .aosDeepLink(aosDeepLink)
                        .iosDeepLink(iosDeepLink)
                        .senderMemberId(senderMemberId)
                        .receiverMemberId(receiverMemberId)
                        .build();
        }
}
