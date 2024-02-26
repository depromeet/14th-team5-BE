package com.oing.restapi;

import com.oing.dto.response.*;
import com.oing.util.security.LoginFamilyId;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/11
 * Time: 10:11 PM
 */
@Tag(name = "캘린더 API", description = "캘린더 관련 API")
@RestController
@Valid
@RequestMapping("/v1/calendar")
public interface CalendarApi {

    @Operation(summary = "월별 캘린더 조회", description = "월별로 캘린더를 조회합니다. 각 날짜의 대표 게시글 정보와 가족 구성원 전부의 업로드 여부가가 조회되며, 해당 날짜에 게시글이 있는 경우만 response 에 포함됩니다.")
    @GetMapping(params = {"type=MONTHLY"})
    ArrayResponse<CalendarResponse> getMonthlyCalendar(
            @RequestParam
            @Parameter(description = "조회할 년월", example = "2021-12")
            String yearMonth,

            @Parameter(hidden = true)
            @LoginFamilyId
            String loginFamilyId
    );

    @Operation(summary = "캘린더 베너 조회", description = "캘린더 상단의 베너를 조회합니다.")
    @GetMapping("/banner")
    BannerResponse getBanner(
            @RequestParam(required = false)
            @Parameter(description = "조회할 년월", example = "2021-12")
            String yearMonth,

            @LoginFamilyId
            @Parameter(hidden = true)
            String loginFamilyId
    );

    @Operation(summary = "캘린더 통계 조회", description = "캘린더의 통계를 조회합니다.")
    @GetMapping("/summary")
    FamilyMonthlyStatisticsResponse getSummary(
            @RequestParam(required = false)
            @Parameter(description = "조회할 년월", example = "2021-12")
            String yearMonth,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );
}
