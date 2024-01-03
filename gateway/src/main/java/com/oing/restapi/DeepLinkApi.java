package com.oing.restapi;

import com.oing.dto.response.DeepLinkResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2024/01/03
 * Time: 10:11 AM
 */
@Tag(name = "링크 관련 API", description = "딥링크 관련 API")
@RestController
@Valid
@RequestMapping("/v1/links")
public interface DeepLinkApi {
    @Operation(summary = "링크 정보 조회", description = "링크 정보를 조회합니다.")
    @GetMapping("/{linkId}")
    DeepLinkResponse getLinkDetails(
            @PathVariable
            @Parameter(description = "링크 ID", example = "bef039df")
            String linkId
    );

    @Operation(summary = "가족 링크 생성", description = "가족 링크를 생성합니다.")
    @PostMapping("/family/{familyId}")
    DeepLinkResponse createFamilyDeepLink(
            @PathVariable
            @Parameter(description = "가족 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            String familyId
    );
}
