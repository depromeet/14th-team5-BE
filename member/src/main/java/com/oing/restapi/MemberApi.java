package com.oing.restapi;

import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.QuitMemberRequest;
import com.oing.dto.request.UpdateMemberNameRequest;
import com.oing.dto.request.UpdateMemberProfileImageUrlRequest;
import com.oing.dto.response.*;
import com.oing.util.security.LoginFamilyId;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 API", description = "회원 관련 API")
@RestController
@Validated
@RequestMapping("/v1/members")
public interface MemberApi {
    @Operation(summary = "가족 구성원 프로필 조회", description = "가족 구성원 프로필을 조회합니다.", parameters = {
            @Parameter(name = "type", description = "가족 구성원 타입", example = "FAMILY", required = true)
    })
    @GetMapping(params = {"type=FAMILY"})
    PaginationResponse<FamilyMemberProfileResponse> getFamilyMembersProfiles(
            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "가져올 현재 페이지", example = "1")
            @Min(value = 1)
            Integer page,

            @RequestParam(required = false, defaultValue = "10")
            @Parameter(description = "가져올 페이지당 크기", example = "10")
            @Min(value = 1)
            Integer size,

            @Parameter(hidden = true)
            @LoginFamilyId
            String familyId
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
            @Valid
            @RequestBody
            PreSignedUrlRequest request
    );

    @Operation(summary = "회원 프로필 이미지 수정", description = "회원 프로필 이미지를 수정합니다.")
    @PutMapping("/profile-image-url/{memberId}")
    MemberResponse updateMemberProfileImageUrl(
            @Parameter(description = "수정할 회원 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String memberId,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId,

            @Valid
            @RequestBody
            UpdateMemberProfileImageUrlRequest request
    );

    @Operation(summary = "회원 프로필 이미지 삭제", description = "회원 프로필 이미지를 삭제합니다.")
    @DeleteMapping("/profile-image-url/{memberId}")
    MemberResponse deleteMemberProfileImageUrl(
            @Parameter(description = "프로필 이미지를 삭제할 회원 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String memberId,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );

    @Operation(summary = "회원 이름 수정", description = "회원 이름을 수정합니다.")
    @PutMapping("/name/{memberId}")
    MemberResponse updateMemberName(
            @Parameter(description = "수정할 회원 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String memberId,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId,

            @Valid
            @RequestBody
            UpdateMemberNameRequest request
    );

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 수행합니다.")
    @DeleteMapping("/{memberId}")
    DefaultResponse deleteMember(
            @Parameter(description = "탈퇴할 회원 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String memberId,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId,

            @RequestBody(required = false) //for api version compatibility
            QuitMemberRequest request
    );
}
