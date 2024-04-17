package com.oing.controller;

import com.oing.dto.response.FamilyMemberMonthlyRankingResponse;
import com.oing.dto.response.FamilyMemberRankerResponse;
import com.oing.restapi.HomeApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class HomeController implements HomeApi {

    @Override
    public FamilyMemberMonthlyRankingResponse getFamilyMemberMonthlyRanking(String loginMemberId, String loginFamilyId) {
        // TODO: API Response Mocking 입니다.

        FamilyMemberRankerResponse first = new FamilyMemberRankerResponse("https://static01.nyt.com/images/2016/09/28/us/28xp-pepefrog/28xp-pepefrog-superJumbo.jpg", "정신적 지주", 24);
        FamilyMemberRankerResponse second = new FamilyMemberRankerResponse("https://static01.nyt.com/images/2016/09/28/us/28xp-pepefrog/28xp-pepefrog-superJumbo.jpg", "권순찬", 23);
        FamilyMemberRankerResponse third = null;

        return new FamilyMemberMonthlyRankingResponse(4, first, second, third);
    }
}
