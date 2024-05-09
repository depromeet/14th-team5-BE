package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 기반 랭커 응답")
public record PostRankerResponse(

        @Schema(description = "멤버 아이디", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String memberId,

        @Schema(description = "게시글 갯수")
        Integer postCount
) {
}
