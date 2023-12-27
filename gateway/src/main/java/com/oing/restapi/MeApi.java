package com.oing.restapi;

import com.oing.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
