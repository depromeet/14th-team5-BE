package com.oing.restapi;

import com.oing.config.support.RequestAppKey;
import com.oing.dto.request.AddFcmTokenRequest;
import com.oing.dto.request.JoinFamilyRequest;
import com.oing.dto.response.AppVersionResponse;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.FamilyResponse;
import com.oing.dto.response.MemberResponse;
import com.oing.util.security.LoginFamilyId;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/27
 * Time: 10:11 PM
 */
@Tag(name = "내 정보 API", description = "내 정보(로그인 유저) 조회하는 API")
@RestController
@Valid
@RequestMapping("/v1/me")
public interface MeApi {
    @Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
    @GetMapping("/member-info")
    MemberResponse getMe(
            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );

    @Operation(summary = "내 가족 정보 조회", description = "내 가족 정보를 조회합니다.")
    @GetMapping("/family-info")
    FamilyResponse getMyFamily(
            @Parameter(hidden = true)
            @LoginFamilyId
            String loginFamilyId
    );

    @Operation(summary = "FCM 토큰 등록", description = "FCM 토큰을 등록합니다.")
    @PostMapping("/fcm")
    DefaultResponse registerFcmToken(
            @Valid
            @RequestBody
            AddFcmTokenRequest request,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );

    @Operation(summary = "FCM 토큰 삭제", description = "FCM 토큰을 삭제합니다.")
    @DeleteMapping("/fcm/{fcmToken}")
    DefaultResponse deleteFcmToken(
            @PathVariable
            @Parameter(description = "삭제하고자 하는 fcmToken", example = "fcmToken")
            String fcmToken,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );


    @Operation(summary = "가족 가입", description = "가족에 가입합니다.")
    @PostMapping("/join-family")
    FamilyResponse joinFamily(
            @Valid
            @RequestBody
            JoinFamilyRequest request,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );

    @Operation(summary = "가족 생성 및 가입", description = "가족을 생성하고 가입합니다.")
    @PostMapping("/create-family")
    FamilyResponse createFamilyAndJoin(
            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );

    @Operation(summary = "내 접속 버전 조회", description = "현재 버전 정보를 조회합니다.")
    @GetMapping("/app-version")
    AppVersionResponse getCurrentAppVersion(
            @RequestAppKey UUID appKey
    );

    @Operation(summary = "가족 탈퇴", description = "가족을 탈퇴합니다.")
    @PostMapping("/quit-family")
    DefaultResponse quitFamily(
            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );

}
