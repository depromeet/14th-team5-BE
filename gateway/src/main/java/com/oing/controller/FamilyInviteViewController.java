package com.oing.controller;

import com.oing.dto.response.DeepLinkResponse;
import com.oing.dto.response.FamilyInviteDeepLinkResponse;
import com.oing.dto.response.MemberResponse;
import com.oing.restapi.DeepLinkApi;
import com.oing.restapi.FamilyInviteViewApi;
import com.oing.service.FamilyBridge;
import com.oing.service.MemberBridge;
import com.oing.service.PostBridge;
import com.oing.restapi.MeApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FamilyInviteViewController implements FamilyInviteViewApi {

    private final DeepLinkApi deepLinkApi;
    private final MemberBridge memberBridge;
    private final FamilyBridge familyBridge;
    private final PostBridge postBridge;
    private final MemberController memberController;

    @Override
    public FamilyInviteDeepLinkResponse getFamilyInviteLinkDetails(String linkId, String loginMemberId) {
        boolean isRequesterJoinedFamily = true;
        MemberResponse me = memberController.getMemberNullable(loginMemberId);
        if (me.familyId() == null || me.memberId() == null) {
            isRequesterJoinedFamily = false;
        }

        DeepLinkResponse deepLinkResponse = deepLinkApi.getLinkDetails(linkId);
        String familyId = deepLinkResponse.getDetails().get("familyId");
        String familyName = familyBridge.findFamilyNameByFamilyId(familyId);
        List<String> familyMemberNames = memberBridge.getFamilyMemberNamesByFamilyId(familyId);
        List<String> familyMemberProfileImgUrls = memberBridge.getFamilyMemberProfileImgUrlsByFamilyId(familyId);
        int familyMemberCount = memberBridge.getFamilyMemberCountByFamilyId(familyId);
        int extraFamilyMemberCount;
        if (familyMemberCount<3) {
            extraFamilyMemberCount = 0;
        } else {
            extraFamilyMemberCount = familyMemberCount - 2;
        }
        int survivalPostCount = postBridge.countSurvivalPostsByFamilyId(familyId);

        String inviterId = deepLinkResponse.getDetails().get("memberId");
        String inviterName = memberBridge.getMemberNameByMemberId(inviterId);

        return new FamilyInviteDeepLinkResponse (
                familyId,
                familyName,
                familyMemberNames,
                familyMemberProfileImgUrls,
                extraFamilyMemberCount,
                familyMemberCount,
                inviterName,
                survivalPostCount,
                isRequesterJoinedFamily
        );
    }
}
