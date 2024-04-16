package com.oing.restapi;

import com.oing.dto.response.FamilyMemberMonthlyRankingResponse;
import com.oing.util.security.LoginFamilyId;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "홈 View API", description = "프론트의 홈 화면 기반으로 작성된 View API")
@RestController
@Valid
@RequestMapping("/v1/home")
public interface HomeApi {

    @Operation(summary = "금월의 가족 구성원 월간 랭킹 조회", description = "이번 달에 해당하는 가족 구성원 월간 랭킹을 조회합니다.")
    FamilyMemberMonthlyRankingResponse getFamilyMemberMonthlyRanking(
            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId,

            @Parameter(hidden = true)
            @LoginFamilyId
            String loginFamilyId
    );
}
