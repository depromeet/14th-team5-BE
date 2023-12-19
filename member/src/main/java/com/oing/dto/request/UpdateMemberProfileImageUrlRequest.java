package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 프로필 이미지 수정 요청")
public record UpdateMemberProfileImageUrlRequest(
        @Schema(description = "회원 프로필 사진 주소", example = "images/profile/01HJ1SEEEFDSA5ZZVRW.jpg")
        String profileImageUrl
) {
}
