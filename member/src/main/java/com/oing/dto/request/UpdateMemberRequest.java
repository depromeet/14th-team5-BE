package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 정보 수정 요청")
public record UpdateMemberRequest(
        @Schema(description = "회원 이름", example = "홍길동")
        String name,

        @Schema(description = "회원 프로필 사진 주소", example = "https://asset.no5ing.kr/member/01HGW2N7EHJVJ4CJ999RRS2E97")
        String profileImageUrl
) {
}
