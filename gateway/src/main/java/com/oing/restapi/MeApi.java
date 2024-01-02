package com.oing.restapi;

import com.oing.dto.request.AddFcmTokenRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    MemberResponse getMe();

    @Operation(summary = "FCM 토큰 등록", description = "FCM 토큰을 등록합니다.")
    @PostMapping("/fcm")
    DefaultResponse registerFcmToken(
            @Valid
            @RequestBody
            AddFcmTokenRequest request
    );

    @Operation(summary = "FCM 토큰 삭제", description = "FCM 토큰을 삭제합니다.")
    @DeleteMapping("/fcm/{fcmToken}")
    DefaultResponse deleteFcmToken(
            @PathVariable
            @Parameter(description = "삭제하고자 하는 fcmToken", example = "fcmToken")
            String fcmToken
    );
}
