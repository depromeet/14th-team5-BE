package com.oing.controller;

import com.oing.dto.response.FamilyInviteDeepLinkResponse;
import com.oing.restapi.DeepLinkApi;
import com.oing.restapi.FamilyInviteViewApi;
import com.oing.restapi.MeApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FamilyInviteViewController implements FamilyInviteViewApi {

    private final DeepLinkApi deepLinkApi;
    private final MeApi meApi;

    @Override
    public FamilyInviteDeepLinkResponse getFamilyInviteLinkDetails(String linkId, String loginMemberId) {

        // TODO : 기능 구현할 때, 응답 모킹용 코드 삭제하고 아래 주석 풀기
        boolean isRequesterJoinedFamily = false;
//        MemberResponse me = meApi.getMe(loginMemberId);
//        if (me.familyId() != null) {
//            isRequesterJoinedFamily = true;
//        }
        //
        if (loginMemberId != null) {
            isRequesterJoinedFamily = true;
        }



        return new FamilyInviteDeepLinkResponse (
                "01HGW2N7EHJVJ4CJ999RRS2E97",
                "사랑하는 우리가족",
                List.of("https://upload.wikimedia.org/wikipedia/en/thumb/6/63/Feels_good_man.jpg/200px-Feels_good_man.jpg", "https://upload.wikimedia.org/wikipedia/en/thumb/6/63/Feels_good_man.jpg/200px-Feels_good_man.jpg"),
                3,
                5,
                "김철수",
                3,
                isRequesterJoinedFamily
        );
    }
}
