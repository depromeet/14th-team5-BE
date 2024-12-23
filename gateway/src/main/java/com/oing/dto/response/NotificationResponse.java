package com.oing.dto.response;

import com.oing.domain.ProfileStyle;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(description = "알림 응답")
public record NotificationResponse(
        @Schema(description = "노티 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String notificationId,

        @Schema(description = "발송자 프로필 이미지 url", example = "https://..")
        String senderProfileImageUrl,

        @Schema(description = "발송자 프로필 스타일", example = "BIRTHDAY")
        ProfileStyle sencerProfileStyle,

        @Schema(description = "알림 제목", example = "우리 가족 모두가 생존신고를 완료했어요")
        String title,

        @Schema(description = "알림 내용", example = "우리 가족 모두가 생존신고를 완료했어요")
        String content,

        @Schema(description = "ios 기준 딥링크", example = "https://..")
        String iosDeepLink,

        @Schema(description = "안드로이드 기준 딥링크", example = "https://..")
        String aosDeepLink,

        @Schema(description = "알림 생성 시간", example = "2023-12-23T01:53:21.577347+09:00")
        ZonedDateTime createdAt
) {
}
