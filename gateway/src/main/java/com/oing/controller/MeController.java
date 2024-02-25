package com.oing.controller;

import com.oing.component.AppVersionCache;
import com.oing.domain.AppVersion;
import com.oing.domain.Family;
import com.oing.domain.FamilyInviteLink;
import com.oing.domain.Member;
import com.oing.dto.request.AddFcmTokenRequest;
import com.oing.dto.request.JoinFamilyRequest;
import com.oing.dto.response.AppVersionResponse;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.FamilyResponse;
import com.oing.dto.response.MemberResponse;
import com.oing.exception.AlreadyInFamilyException;
import com.oing.exception.FamilyNotFoundException;
import com.oing.restapi.MeApi;
import com.oing.service.FamilyInviteLinkService;
import com.oing.service.FamilyService;
import com.oing.service.MemberDeviceService;
import com.oing.service.MemberService;
import com.oing.util.AuthenticationHolder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.security.InvalidParameterException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MeController implements MeApi {
    private final AuthenticationHolder authenticationHolder;
    private final MemberService memberService;
    private final MemberDeviceService memberDeviceService;
    private final FamilyService familyService;
    private final FamilyInviteLinkService familyInviteLinkService;
    private final AppVersionCache appVersionCache;

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
        if(memberId == null || memberId.length() != 26) throw new InvalidParameterException();

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
        log.info("Member {} is trying to join to family", memberId);
        FamilyInviteLink link = familyInviteLinkService.retrieveDeepLinkDetails(request.inviteCode());
        Family targetFamily = familyService.getFamilyById(link.getFamilyId());

        Member member = memberService.findMemberById(memberId);
        // TODO: iOS 업데이트 이슈로 온보딩 플로우에 갖힌 유저를 위해 일시적으로 예외 핸들링 주석 처리 !!!
        //        if (member.hasFamily()) throw new AlreadyInFamilyException();
        member.setFamilyId(targetFamily.getId());
        log.info("Member {} has joined to family {}", memberId, targetFamily.getId());

        return FamilyResponse.of(targetFamily);
    }

    @Transactional
    @Override
    public FamilyResponse createFamilyAndJoin() {
        String memberId = authenticationHolder.getUserId();
        log.info("Member {} is trying to create a family", memberId);
        Member member = memberService.findMemberById(memberId);
        // TODO: iOS 업데이트 이슈로 온보딩 플로우에 갖힌 유저를 위해 일시적으로 예외 핸들링 주석 처리 !!!
        //        if (member.hasFamily()) throw new AlreadyInFamilyException();

        Family family = familyService.createFamily();
        member.setFamilyId(family.getId());
        log.info("Member {} has created and joined to a family", memberId);
        return FamilyResponse.of(family);
    }


    @Override
    public AppVersionResponse getCurrentAppVersion(UUID appKey) {
        AppVersion appVersion = appVersionCache.getAppVersion(appKey);
        return AppVersionResponse.from(appVersion);
    }

    @Transactional
    @Override
    public DefaultResponse quitFamily() {
        String memberId = authenticationHolder.getUserId();
        log.info("Member {} is trying to quit from family", memberId);
        Member member = memberService.findMemberById(memberId);
        if (!member.hasFamily()) {
            log.warn("Member {} is not in any family", memberId);
            throw new FamilyNotFoundException();
        }
        member.setFamilyId(null);
        log.info("Member {} has quit from family", memberId);
        return DefaultResponse.ok();
    }
}
