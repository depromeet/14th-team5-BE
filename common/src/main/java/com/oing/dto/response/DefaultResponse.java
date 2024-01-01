package com.oing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2024/01/01
 * Time: 12:30 PM
 */
@Schema(description = "기본 Operation 응답")
public record DefaultResponse(
        @Schema(description = "성공 유무", example = "true")
        boolean success
) {
        public static DefaultResponse ok() {
                return new DefaultResponse(true);
        }
}
