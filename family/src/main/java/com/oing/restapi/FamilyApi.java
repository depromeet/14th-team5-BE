package com.oing.restapi;

import com.oing.dto.request.UpdateFamilyNameRequest;
import com.oing.dto.response.FamilyResponse;
import com.oing.util.security.LoginFamilyId;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "가족 API")
@RestController
@Valid
@RequestMapping("/v1/families")
public interface FamilyApi {
    @Operation(summary = "가족 생성", description = "가족을 생성합니다.")
    @PostMapping
    FamilyResponse createFamily();

    @Operation(summary = "가족 그룹 생성 시간 조회", description = "가족 그룹 생성 시간을 조회합니다.")
    @GetMapping("/{familyId}/created-at")
    FamilyResponse getFamilyCreatedAt(
            @Parameter(description = "가족 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String familyId,

            @Parameter(hidden = true)
            @LoginFamilyId
            String loginFamilyId
    );

    @Operation(summary = "가족 이름 변경", description = "가족 이름을 변경합니다.")
    @PutMapping("/{familyId}/name")
    FamilyResponse updateFamilyName(
            @Parameter(description = "가족 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String familyId,

            @Parameter(hidden = true)
            @LoginFamilyId
            String loginFamilyId,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId,

            @RequestBody
            UpdateFamilyNameRequest request
    );
}
