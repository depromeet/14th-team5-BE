package com.oing.controller;

import com.oing.domain.model.Member;
import com.oing.dto.response.MemberResponse;
import com.oing.restapi.MeApi;
import com.oing.service.MemberService;
import com.oing.util.AuthenticationHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class MeController implements MeApi {
    private final AuthenticationHolder authenticationHolder;
    private final MemberService memberService;

    @Override
    public MemberResponse getMe() {
        String memberId = authenticationHolder.getUserId();
        Member member = memberService.findMemberById(memberId);
        return MemberResponse.of(member);
    }
}
