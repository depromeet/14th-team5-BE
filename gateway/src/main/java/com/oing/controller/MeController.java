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
import com.oing.exception.FamilyNotFoundException;
import com.oing.restapi.MeApi;
import com.oing.service.FamilyInviteLinkService;
import com.oing.service.FamilyService;
import com.oing.service.MemberDeviceService;
import com.oing.service.MemberService;
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
    private final MemberService memberService;
    private final MemberDeviceService memberDeviceService;
    private final FamilyService familyService;
    private final FamilyInviteLinkService familyInviteLinkService;
    private final AppVersionCache appVersionCache;

    @Override
    public MemberResponse getMe(String loginMemberId) {
        Member member = memberService.getMemberByMemberId(loginMemberId);
        return MemberResponse.of(member);
    }

    @Override
    public DefaultResponse registerFcmToken(
            AddFcmTokenRequest request, String loginMemberId
    ) {
        String token = request.fcmToken();
        if(loginMemberId == null || loginMemberId.length() != 26) throw new InvalidParameterException();

        boolean result = memberDeviceService.addDevice(loginMemberId, token);
        return new DefaultResponse(result);
    }

    @Override
    public DefaultResponse deleteFcmToken(
            String fcmToken, String loginMemberId
    ) {
        boolean result = memberDeviceService.removeDevice(loginMemberId, fcmToken);
        return new DefaultResponse(result);
    }


    @Transactional
    @Override
    public FamilyResponse joinFamily(JoinFamilyRequest request, String loginMemberId) {
        log.info("Member {} is trying to join to family", loginMemberId);
        FamilyInviteLink link = familyInviteLinkService.retrieveDeepLinkDetails(request.inviteCode());
        Family targetFamily = familyService.getFamilyById(link.getFamilyId());

        Member member = memberService.getMemberByMemberId(loginMemberId);
        // TODO: iOS 업데이트 이슈로 온보딩 플로우에 갖힌 유저를 위해 일시적으로 예외 핸들링 주석 처리 !!!
        //        if (member.hasFamily()) throw new AlreadyInFamilyException();
        member.setFamilyId(targetFamily.getId());
        log.info("Member {} has joined to family {}", loginMemberId, targetFamily.getId());

        return FamilyResponse.of(targetFamily);
    }

    @Transactional
    @Override
    public FamilyResponse createFamilyAndJoin(String loginMemberId) {
        log.info("Member {} is trying to create a family", loginMemberId);
        Member member = memberService.getMemberByMemberId(loginMemberId);
        // TODO: iOS 업데이트 이슈로 온보딩 플로우에 갖힌 유저를 위해 일시적으로 예외 핸들링 주석 처리 !!!
        //        if (member.hasFamily()) throw new AlreadyInFamilyException();

        Family family = familyService.createFamily();
        member.setFamilyId(family.getId());
        log.info("Member {} has created and joined to a family", loginMemberId);
        return FamilyResponse.of(family);
    }


    @Override
    public AppVersionResponse getCurrentAppVersion(UUID appKey) {
        AppVersion appVersion = appVersionCache.getAppVersion(appKey);
        return AppVersionResponse.from(appVersion);
    }

    @Transactional
    @Override
    public DefaultResponse quitFamily(String loginMemberId) {
        log.info("Member {} is trying to quit from family", loginMemberId);
        Member member = memberService.getMemberByMemberId(loginMemberId);
        if (!member.hasFamily()) {
            log.warn("Member {} is not in any family", loginMemberId);
            throw new FamilyNotFoundException();
        }
        member.setFamilyId(null);
        log.info("Member {} has quit from family", loginMemberId);
        return DefaultResponse.ok();
    }
}
