package com.oing.restapi;

import com.oing.dto.response.FamilyMemberProfileResponse;
import com.oing.dto.response.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;
import com.oing.dto.response.MemberResponse;

@Tag(name = "회원 API", description = "회원 관련 API")
@RestController
@Valid
@RequestMapping("/v1/members")
public interface MemberApi {
    @Operation(summary = "가족 구성원 프로필 조회", description = "가족 구성원 프로필을 조회합니다.")
    @GetMapping(value = "/profile", params = {"type=FAMILY"})
    PaginationResponse<FamilyMemberProfileResponse> getFamilyMemberProfile(
            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "가져올 현재 페이지", example = "1")
            @Min(value = 1)
            Integer page,

            @RequestParam(required = false, defaultValue = "10")
            @Parameter(description = "가져올 페이지당 크기", example = "10")
            @Min(value = 1)
            Integer size
    );

    @GetMapping("/{memberId}")
    MemberResponse getMember(@PathVariable String memberId);
}
