package com.oing.controller;

import com.oing.exception.DomainException;
import com.oing.exception.ErrorCode;
import com.oing.domain.Family;
import com.oing.domain.FamilyInviteLink;
import com.oing.domain.Member;
import com.oing.dto.request.AddFcmTokenRequest;
import com.oing.dto.request.JoinFamilyRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.FamilyResponse;
import com.oing.dto.response.MemberResponse;
import com.oing.exception.AlreadyInFamilyException;
import com.oing.restapi.MeApi;
import com.oing.service.FamilyInviteLinkService;
import com.oing.service.FamilyService;
import com.oing.service.MemberDeviceService;
import com.oing.service.MemberService;
import com.oing.util.AuthenticationHolder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class MeController implements MeApi {
    private final AuthenticationHolder authenticationHolder;
    private final MemberService memberService;
    private final MemberDeviceService memberDeviceService;
    private final FamilyService familyService;
    private final FamilyInviteLinkService familyInviteLinkService;

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


    @Transactional
    @Override
    public FamilyResponse joinFamily(JoinFamilyRequest request) {
        String memberId = authenticationHolder.getUserId();
        FamilyInviteLink link = familyInviteLinkService.retrieveDeepLinkDetails(request.inviteCode());
        Family targetFamily = familyService.getFamilyById(link.getFamilyId());

        Member member = memberService.findMemberById(memberId);
        if (member.getFamilyId() != null) throw new AlreadyInFamilyException();
        member.setFamilyId(targetFamily.getId());

        return FamilyResponse.of(targetFamily);
    }

    @Transactional
    @Override
    public FamilyResponse createFamilyAndJoin() {
        String memberId = authenticationHolder.getUserId();
        Member member = memberService.findMemberById(memberId);
        if (member.hasFamily()) throw new DomainException(ErrorCode.UNKNOWN_SERVER_ERROR);

        Family family = familyService.createFamily();
        member.setFamilyId(family.getId());
        return FamilyResponse.of(family);
    }
}
