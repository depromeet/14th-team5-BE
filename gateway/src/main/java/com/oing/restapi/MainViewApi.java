package com.oing.restapi;

import com.oing.dto.response.DaytimePageResponse;
import com.oing.dto.response.FamilyMemberMonthlyRankingResponse;
import com.oing.dto.response.NighttimePageResponse;
import com.oing.util.security.LoginFamilyId;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/16/24
 * Time: 4:01 PM
 */
@Tag(name = "메인페이지 뷰 기반 API", description = "프론트의 메인페이지 기반으로 작성된 View API입니다.")
@RestController
@RequestMapping("/v1/view/main")
public interface MainViewApi {

    @Operation(summary = "주간의 메인 페이지 조회")
    @GetMapping("/daytime-page")
    DaytimePageResponse getDaytimePage(

            @RequestParam(required = false, defaultValue = "true")
            @Parameter(description = "(디버그용) 미션 해금 여부 조작 필드", example = "true")
            boolean isMissionUnlocked,

            @RequestParam(required = false, defaultValue = "true")
            @Parameter(description = "(디버그용) 오늘 나 업로드 여부 조작 필드", example = "true")
            boolean isMeUploadedToday,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );


    @Operation(summary = "야간의 메인 페이지 조회")
    @GetMapping("/nighttime-page")
    NighttimePageResponse getNighttimePage(
            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId,

            @Parameter(hidden = true)
            @LoginFamilyId
            String loginFamilyId
    );


    @Operation(summary = "금월의 가족 구성원 월간 랭킹 조회", description = "이번 달에 해당하는 가족 구성원 월간 랭킹을 조회합니다.")
    @GetMapping("/family-ranking")
    FamilyMemberMonthlyRankingResponse getFamilyMemberMonthlyRanking(
            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId,

            @Parameter(hidden = true)
            @LoginFamilyId
            String loginFamilyId
    );
}
