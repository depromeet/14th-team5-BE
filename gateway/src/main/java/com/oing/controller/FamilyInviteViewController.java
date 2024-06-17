package com.oing.controller;

import com.oing.domain.Member;
import com.oing.dto.response.DeepLinkResponse;
import com.oing.dto.response.FamilyInviteDeepLinkResponse;
import com.oing.dto.response.MemberResponse;
import com.oing.restapi.DeepLinkApi;
import com.oing.restapi.FamilyInviteViewApi;
import com.oing.restapi.MeApi;
import com.oing.service.MemberBridge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FamilyInviteViewController implements FamilyInviteViewApi {

    private final DeepLinkApi deepLinkApi;
    private final MeApi meApi;
    private final MemberBridge memberBridge;

    @Override
    public FamilyInviteDeepLinkResponse getFamilyInviteLinkDetails(String linkId, String loginMemberId) {

        // TODO : 기능 구현할 때, 응답 모킹용 코드 삭제하고 아래 주석 풀기
        boolean isRequesterJoinedFamily = false;
        MemberResponse me = meApi.getMe(loginMemberId);
        if (me.familyId() != null) {
            isRequesterJoinedFamily = true;
        }
        if (loginMemberId != null) {
            isRequesterJoinedFamily = true;
        }

        DeepLinkResponse deepLinkResponse = deepLinkApi.getLinkDetails(linkId);
        String familyId = deepLinkResponse.getDetails().get("familyId");
        String inviterId = deepLinkResponse.getDetails().get("memberId");
        String inviterName = memberBridge.getMemberNameByMemberId(inviterId);
        // TODO: 가족 이름 호출
        // TODO: 가족 프로필 사진 리스트 호출
        // TODO: 가족 멤버 카운트
        // TODO: 생존신고 횟수

        return new FamilyInviteDeepLinkResponse (
                familyId,
                "사랑하는 우리가족",
                List.of("https://upload.wikimedia.org/wikipedia/en/thumb/6/63/Feels_good_man.jpg/200px-Feels_good_man.jpg", "https://upload.wikimedia.org/wikipedia/en/thumb/6/63/Feels_good_man.jpg/200px-Feels_good_man.jpg"),
                3,
                5,
                inviterName,
                3,
                isRequesterJoinedFamily
        );
    }
}
