package com.oing.controller;

import com.oing.dto.response.FamilyInvitationLinkResponse;
import com.oing.dto.response.FamilyMonthlyStatisticsResponse;
import com.oing.dto.response.FamilyResponse;
import com.oing.restapi.FamilyApi;
import org.springframework.stereotype.Controller;

@Controller
public class FamilyController implements FamilyApi {
    @Override
    public FamilyMonthlyStatisticsResponse getMonthlyFamilyStatistics(String familyId, String yearMonth) {
        return null;
    }

    @Override
    public FamilyResponse createFamily() {
        return null;
    }

    @Override
    public FamilyInvitationLinkResponse getInvitationLink(String familyId) {
        return null;
    }
}
