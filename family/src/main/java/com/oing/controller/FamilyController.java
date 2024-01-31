package com.oing.controller;

import com.oing.domain.Family;
import com.oing.dto.response.FamilyMonthlyStatisticsResponse;
import com.oing.dto.response.FamilyResponse;
import com.oing.restapi.FamilyApi;
import com.oing.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Random;

@RequiredArgsConstructor
@Controller
public class FamilyController implements FamilyApi {
    private final FamilyService familyService;

    @Override
    public FamilyResponse createFamily() {
        Family family = familyService.createFamily();
        return FamilyResponse.of(family);
    }

    @Override
    public FamilyResponse getFamilyCreatedAt(String familyId) {
        return new FamilyResponse(familyId, familyService.findFamilyCreatedAt(familyId));
    }
}
