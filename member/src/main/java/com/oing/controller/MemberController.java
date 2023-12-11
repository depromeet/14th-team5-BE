package com.oing.controller;

import com.oing.restapi.MemberApi;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController implements MemberApi {

//    @Override
//    public PaginationResponse<FamilyMemberProfileResponse> getFamilyMemberProfile(Integer page, Integer size) {
//        String memberIdBase = "01HGW2N7EHJVJ4CJ999RRS2E";
//        String memberNameBase = "디프만";
//
//        List<FamilyMemberProfileResponse> mockResponses = new ArrayList<>();
//        for(int i = 0; i < size; i++) {
//            int currentIndex = i + ((page - 1) * size);
//            String suffix = String.format("%02d", currentIndex);
//            mockResponses.add(
//                    new FamilyMemberProfileResponse(
//                            memberIdBase + suffix,
//                            memberNameBase + suffix,
//                            "https://picsum.photos/200/300?random=" + currentIndex
//                    )
//            );
//        }
//
//        return new PaginationResponse<>(page, 3, size, mockResponses);
//    }
}
