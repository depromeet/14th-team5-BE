package com.oing.controller;

import com.oing.domain.DeepLink;
import com.oing.domain.DeepLinkType;
import com.oing.domain.FamilyInviteLink;
import com.oing.domain.SerializableDeepLink;
import com.oing.dto.response.DeepLinkResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.LinkNotValidException;
import com.oing.restapi.DeepLinkApi;
import com.oing.service.DeepLinkDetailService;
import com.oing.service.DeepLinkService;
import com.oing.service.MemberBridge;
import com.oing.util.AuthenticationHolder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 1/3/24
 * Time: 10:17 AM
 */
@RequiredArgsConstructor
@Controller
public class DeepLinkController implements DeepLinkApi {
    public static String FAMILY_LINK_PREFIX = "https://no5ing.kr/o/";

    private final AuthenticationHolder authenticationHolder;
    private final DeepLinkService deepLinkService;
    private final MemberBridge memberBridge;
    private final DeepLinkDetailService<FamilyInviteLink> familyDeepLinkService;

    @Transactional
    @Override
    public DeepLinkResponse getLinkDetails(String linkId) {
        DeepLink deepLink = deepLinkService.getDeepLink(linkId);
        DeepLinkDetailService<? extends SerializableDeepLink> deepLinkService = switch (deepLink.getType()) {
            case FAMILY_REGISTRATION -> familyDeepLinkService;
            default -> throw new LinkNotValidException();
        };

        SerializableDeepLink deepLinkDetails = deepLinkService.retrieveDeepLinkDetails(linkId);
        return new DeepLinkResponse(
                deepLink.getLinkId(),
                generateLink(deepLink.getLinkId()),
                deepLink.getType(),
                deepLinkDetails.serialize()
        );
    }

    @Transactional
    @Override
    public DeepLinkResponse createFamilyDeepLink(
            String familyId
    ) {
        String linkId = deepLinkService.generateNewDeepLinkId();
        String currentMemberId = authenticationHolder.getUserId();
        String memberFamilyId = memberBridge.getFamilyIdByMemberId(currentMemberId);

        // 내 가족 외의 딥링크를 생성하려는 경우
        if (!Objects.equals(memberFamilyId, familyId)) throw new AuthorizationFailedException();

        FamilyInviteLink newInviteLink = new FamilyInviteLink(linkId, familyId);
        FamilyInviteLink familyInviteLink = familyDeepLinkService.findPriorDeepLinkDetails(newInviteLink);
        if (familyInviteLink != null) {
            return getLinkDetails(familyInviteLink.getLinkId());
        }
        newInviteLink = familyDeepLinkService.storeDeepLinkDetails(newInviteLink);

        DeepLink deepLink = deepLinkService.createDeepLink(linkId, DeepLinkType.FAMILY_REGISTRATION);
        return new DeepLinkResponse(
                deepLink.getLinkId(),
                generateLink(deepLink.getLinkId()),
                deepLink.getType(),
                newInviteLink.serialize()
        );
    }

    private String generateLink(String linkId) {
        return FAMILY_LINK_PREFIX + linkId;
    }
}
