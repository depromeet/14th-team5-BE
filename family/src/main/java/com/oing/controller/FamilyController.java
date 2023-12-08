package com.oing.controller;

import com.oing.dto.response.FamilyMemberProfileResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.restapi.FamilyApi;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FamilyController implements FamilyApi {
    @Override
    public PaginationResponse<FamilyMemberProfileResponse> getFamilyMemberProfile(Integer page, Integer size) {
        String memberIdBase = "01HGW2N7EHJVJ4CJ999RRS2E";
        String memberNameBase = "디프만";

        List<FamilyMemberProfileResponse> mockResponses = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            int currentIndex = i + ((page - 1) * size);
            String suffix = String.format("%02d", currentIndex);
            mockResponses.add(
                    new FamilyMemberProfileResponse(
                            memberIdBase + suffix,
                            memberNameBase + suffix,
                            "https://picsum.photos/200/300?random=" + currentIndex
                    )
            );
        }

        return new PaginationResponse<>(page, 3, size, mockResponses);
    }
}
