package com.oing.controller;

import com.oing.dto.response.FamilyCreatedAtResponse;
import com.oing.dto.response.FamilyInvitationLinkResponse;
import com.oing.dto.response.FamilyMonthlyStatisticsResponse;
import com.oing.dto.response.FamilyResponse;
import com.oing.restapi.FamilyApi;
import com.oing.service.FamilyService;
import com.oing.util.IdentityGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.ZonedDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Controller
public class FamilyController implements FamilyApi {
    private final IdentityGenerator identityGenerator;
    private final FamilyService familyService;

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
        return new FamilyResponse(identityGenerator.generateIdentity(), ZonedDateTime.now());
    }

    @Override
    public FamilyInvitationLinkResponse getInvitationLink(String familyId) {
        return new FamilyInvitationLinkResponse("https://no5ing.kr/o/bgE39230f");
    }

    @Override
    public FamilyCreatedAtResponse getFamilyCreatedAt(String familyId) {
        return new FamilyCreatedAtResponse(familyService.findFamilyCreatedAt(familyId));
    }
}
