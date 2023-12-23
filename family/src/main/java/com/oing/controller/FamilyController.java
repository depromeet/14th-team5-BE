package com.oing.controller;

import com.oing.domain.model.Family;
import com.oing.dto.response.FamilyInvitationLinkResponse;
import com.oing.dto.response.FamilyMonthlyStatisticsResponse;
import com.oing.dto.response.FamilyResponse;
import com.oing.restapi.FamilyApi;
import com.oing.service.FamilyService;
import com.oing.util.IdentityGenerator;
import com.oing.service.FamilyInviteLinkService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Random;

@RequiredArgsConstructor
@Controller
public class FamilyController implements FamilyApi {
    private final IdentityGenerator identityGenerator;
    private final FamilyService familyService;
    private final FamilyInviteLinkService familyInviteLinkService;

    @Override
    public FamilyMonthlyStatisticsResponse getMonthlyFamilyStatistics(String familyId, String yearMonth) {
        Random random = new Random();
        return new FamilyMonthlyStatisticsResponse(
                random.nextInt(3,5),
                random.nextInt(5, 10),
                random.nextInt(0, 3)
        );
    }

    @Override
    public FamilyResponse createFamily() {
        Family family = familyService.createFamily();
        return FamilyResponse.of(family);
    }

    @Transactional
    @Override
    public FamilyInvitationLinkResponse getInvitationLink(String familyId) {
        Family family = familyService.getFamilyById(familyId);
        String link = familyInviteLinkService.generateFamilyInviteLink(family);
        return new FamilyInvitationLinkResponse(link);
    }

    @Override
    public FamilyResponse getFamilyCreatedAt(String familyId) {
        return new FamilyResponse(familyId, familyService.findFamilyCreatedAt(familyId));
    }
}
