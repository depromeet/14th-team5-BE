package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/16/24
 * Time: 4:07 PM
 */
@Schema(description = "메인 페이지 상단 바 요소")
public record MainPageTopBarResponse(
        @Schema(description = "멤버ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String memberId,

        @Schema(description = "이미지 URL", example = "https://no5ing.com/image/01HGW2N7EHJVJ4CJ999RRS2E97")
        String imageUrl,

        @Schema(description = "이미지 없을 때 표시할 글자", example = "엄")
        String noImageLetter,

        @Schema(description = "닉네임", example = "엄마")
        String displayName,

        @Schema(description = "순위 표기 (1부터 시작)" +
                "\n- 야간의 경우 모두 NULL입니다."
                , example = "1")
        Integer displayRank,

        @Schema(description = "생일 마크 표시 여부", example = "true")
        boolean shouldShowBirthdayMark,

        @Schema(description = "찌르기 아이콘 표시 여부", example = "true")
        boolean shouldShowPickIcon
) {
}
