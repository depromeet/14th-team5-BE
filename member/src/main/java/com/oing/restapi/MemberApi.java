package com.oing.restapi;

import com.oing.dto.request.UpdateMemberRequest;
import com.oing.dto.response.FamilyMemberProfileResponse;
import com.oing.dto.response.MemberResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 API", description = "회원 관련 API")
@RestController
@Valid
@RequestMapping("/v1/members")
public interface MemberApi {
    @Operation(summary = "가족 구성원 프로필 조회", description = "가족 구성원 프로필을 조회합니다.", parameters = {
            @Parameter(name = "type", description = "가족 구성원 타입", example = "FAMILY", required = true)
    })
    @GetMapping(params = {"type=FAMILY"})
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

    @Operation(summary = "회원 조회", description = "회원을 조회합니다.")
    @GetMapping("/{memberId}")
    MemberResponse getMember(
            @Parameter(description = "조회할 회원 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String memberId
    );

    @Operation(summary = "회원 프로필 사진 S3 Presigned Url 요청", description = "S3 Presigned Url을 요청합니다.")
    @PostMapping("/image-upload-request")
    PreSignedUrlResponse requestPresignedUrl(
            @Parameter(description = "이미지 이름(확장자 포함)", example = "image.jpg")
            String imageName
    );

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @PutMapping
    MemberResponse updateMember(
            @Valid
            @RequestBody
            UpdateMemberRequest request
    );

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 수행합니다.")
    @DeleteMapping("/{memberId}")
    void deleteMember(
            @Parameter(description = "탈퇴할 회원 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String memberId
    );
}
