package com.oing.restapi;

import com.oing.dto.response.FamilyInvitationLinkResponse;
import com.oing.dto.response.FamilyMonthlyStatisticsResponse;
import com.oing.dto.response.FamilyResponse;
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
    @Operation(summary = "가족 요약 정보 조회", description = "월별 가족 요약 정보를 조회합니다.")
    @GetMapping("/{familyId}/summary")
    FamilyMonthlyStatisticsResponse getMonthlyFamilyStatistics(
            @Parameter(description = "조회할 가족 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String familyId,

            @RequestParam(required = false)
            @Parameter(description = "조회할 년월", example = "2021-12")
            String yearMonth
    );

    @Operation(summary = "가족 생성", description = "가족을 생성합니다.")
    @PostMapping
    FamilyResponse createFamily();

    @Operation(summary = "가족 그룹 생성 시간 조회", description = "가족 그룹 생성 시간을 조회합니다.")
    @GetMapping("/{familyId}/created-at")
    FamilyResponse getFamilyCreatedAt(
            @Parameter(description = "가족 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String familyId
    );
}
