package com.oing.dto.request;

import com.oing.domain.MemberQuitReasonType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2024/01/13
 * Time: 11:13 PM
 */
@Schema(description = "사용자 회원탈퇴 요청")
public record QuitMemberRequest(
        @Schema(description = "탈퇴 사유", example = "NO_FREQUENTLY_USE")
        MemberQuitReasonType reasonId
) {
}
