package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/16/24
 * Time: 4:07 PM
 */
@Schema(description = "메인 페이지 날 찌른 사람")
public record MainPagePickerResponse(
        @Schema(description = "멤버ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String memberId,

        @Schema(description = "이미지 URL", example = "https://no5ing.com/image/01HGW2N7EHJVJ4CJ999RRS2E97")
        String imageUrl,

        @Schema(description = "사용자 이름", example = "엄마")
        String displayName
) {
}
