package com.oing.controller;

import com.oing.domain.Family;
import com.oing.dto.request.UpdateFamilyNameRequest;
import com.oing.dto.response.FamilyResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.restapi.FamilyApi;
import com.oing.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

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
    public FamilyResponse getFamilyCreatedAt(String familyId, String loginFamilyId) {
        if (!familyId.equals(loginFamilyId)) {
            throw new AuthorizationFailedException();
        }

        Family family = familyService.getFamilyById(familyId);
        return FamilyResponse.of(family);
    }

    @Override
    public FamilyResponse updateFamilyName(String familyId, String loginFamilyId, String loginMemberId, UpdateFamilyNameRequest request) {
        if (!familyId.equals(loginFamilyId)) {
            throw new AuthorizationFailedException();
        }

        Family family = familyService.updateFamilyName(familyId, loginMemberId, request.familyName());
        return FamilyResponse.of(family);
    }
}
