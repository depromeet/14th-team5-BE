package com.oing.controller;

import com.oing.domain.CreateNewUserDTO;
import com.oing.domain.SocialLoginProvider;
import com.oing.domain.SocialLoginResult;
import com.oing.domain.TokenPair;
import com.oing.domain.model.Member;
import com.oing.dto.request.NativeSocialLoginRequest;
import com.oing.dto.response.AuthResultResponse;
import com.oing.restapi.AuthApi;
import com.oing.service.AuthService;
import com.oing.service.MemberService;
import com.oing.service.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:04 AM
 */
@RequiredArgsConstructor
@Controller
public class AuthController implements AuthApi {
    private final AuthService authService;
    private final MemberService memberService;
    private final TokenGenerator tokenGenerator;

    @Override
    public AuthResultResponse socialLogin(String provider, NativeSocialLoginRequest request) {
        // oAuth 로그인 검증 (Apple 등)
        SocialLoginProvider socialLoginProvider = SocialLoginProvider.valueOf(provider.toUpperCase());
        SocialLoginResult socialLoginResult = authService
                .authenticateFromProvider(socialLoginProvider, request.accessToken());

        // 위 결과에서 나온 identifier로 이미 있는 사용자인지 확인
        Member member = memberService
                .findMemberBySocialMemberKey(socialLoginProvider, socialLoginResult.identifier())
                .orElseGet(() -> {
                    //없으면 사용자 회원가입 (생성)
                    CreateNewUserDTO createNewUserDTO = new CreateNewUserDTO(
                            socialLoginProvider,
                            socialLoginResult.identifier());
                    return memberService.createNewMember(createNewUserDTO);
                });

        //사용자로 토큰 생성
        TokenPair tokenPair = tokenGenerator.generateTokenPair(member.getId());
        return AuthResultResponse.of(tokenPair);
    }
}
