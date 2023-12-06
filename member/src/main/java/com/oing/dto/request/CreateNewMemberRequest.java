package com.oing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/06
 * Time: 11:13 PM
 */
@Schema(description = "사용자 회원가입 요청")
public record CreateNewMemberRequest(
        @NotNull
        @Size(min = 1, max = 10)
        @Schema(description = "사용자 이름", example = "송영민")
        String memberName,

        @NotNull
        @Schema(description = "생년월일 yyyy-MM-dd", example = "2001-01-25")
        LocalDate dayOfBirth,

        @Schema(description = "프로필 이미지 URL", example = "https://no5ing.com/profile/1")
        String profileImgUrl
) {
}
