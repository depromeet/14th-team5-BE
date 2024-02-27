package com.oing.controller;

import com.oing.domain.TokenPair;
import com.oing.dto.response.AuthResultResponse;
import com.oing.dto.response.DefaultResponse;
import com.oing.service.MemberPostService;
import com.oing.service.TokenGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Profile("!prod")
public class DevController  {
    private final TokenGenerator tokenGenerator;
    private final MemberPostService memberPostService;

    @Operation(summary = "게시물 삭제", description = "ID를 통해 게시물을 삭제합니다.")
    @DeleteMapping("/v1/posts/{postId}")
    DefaultResponse deletePost(
            @PathVariable
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            String postId
    ) {
        memberPostService.deleteMemberPostById(postId);
        return DefaultResponse.ok();
    }

    @Operation(summary = "토큰 강제 발행", description = "토큰을 강제로 발행합니다.")
    @PostMapping("/v1/auth/force-token")
    AuthResultResponse forceToken(
            @RequestParam("memberId") String memberId
    ) {
        TokenPair tokenPair = tokenGenerator.generateTokenPair(memberId);
        return AuthResultResponse.of(tokenPair, false);
    }
}
