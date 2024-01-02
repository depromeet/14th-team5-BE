package com.oing.controller;

import com.oing.domain.model.Member;
import com.oing.dto.request.AddFcmTokenRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.MemberResponse;
import com.oing.restapi.MeApi;
import com.oing.service.MemberDeviceService;
import com.oing.service.MemberService;
import com.oing.util.AuthenticationHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class MeController implements MeApi {
    private final AuthenticationHolder authenticationHolder;
    private final MemberService memberService;
    private final MemberDeviceService memberDeviceService;

    @Override
    public MemberResponse getMe() {
        String memberId = authenticationHolder.getUserId();
        Member member = memberService.findMemberById(memberId);
        return MemberResponse.of(member);
    }

    @Override
    public DefaultResponse registerFcmToken(
            AddFcmTokenRequest request
    ) {
        String memberId = authenticationHolder.getUserId();
        String token = request.fcmToken();

        boolean result = memberDeviceService.addDevice(memberId, token);
        return new DefaultResponse(result);
    }

    @Override
    public DefaultResponse deleteFcmToken(
            String fcmToken
    ) {
        String memberId = authenticationHolder.getUserId();
        boolean result = memberDeviceService.removeDevice(memberId, fcmToken);
        return new DefaultResponse(result);
    }
}
