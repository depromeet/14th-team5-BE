package com.oing.restapi;

import com.oing.dto.response.*;
import com.oing.util.security.FamilyId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "월별 썸네일 조회", description = "월별로 캘린더의 대표 게시물 썸네일을 조회합니다. 각 날짜의 대표 게시글 정보가 조회되며, 해당 날짜에 게시글이 있는 경우만 response 에 포함됩니다.\n\n ⚠️ 더 이상 type=MONTHLY 를 사용하지 않습니다.")
    @GetMapping("/thumbnails")
    ArrayResponse<CalendarResponse> getMonthlyCalendar(
            @RequestParam
            @Parameter(description = "조회할 년월", example = "2021-12")
            String yearMonth,

            @Parameter(hidden = true)
            @FamilyId
            String familyId
    );

    @Operation(summary = "월별 이벤트 조회", description = "월별로 존재하는 캘린더의 이벤트들을 조회합니다. 모든 가족 구성원이 업로드한 여부 등이 이벤트에 속하며, 해당 날짜에 게시글이 있는 경우만 response 에 포함됩니다.\n\n ⚠️ 월별 캘린더 조회 API와 반환되는 날짜가 다를 수 있습니다.\n\n ⚠️ 더 이상 type=MONTHLY 를 사용하지 않습니다.")
    @GetMapping(value = "/events", params = {"type=MONTHLY"})
    ArrayResponse<DayEventResponse> getMonthlyEvents(
            @RequestParam
            @Parameter(description = "조회할 년월", example = "2021-12")
            String yearMonth,

            @Parameter(hidden = true)
            @FamilyId
            String familyId
    );

    @Operation(summary = "캘린더 베너 조회", description = "캘린더 상단의 베너를 조회합니다.")
    @GetMapping("/banner")
    BannerResponse getBanner(
            @RequestParam(required = false)
            @Parameter(description = "조회할 년월", example = "2021-12")
            String yearMonth,

            @FamilyId
            @Parameter(hidden = true)
            String familyId
    );

    @Operation(summary = "캘린더 통계 조회", description = "캘린더의 통계를 조회합니다.")
    @GetMapping("/summary")
    FamilyMonthlyStatisticsResponse getSummary(
            @RequestParam(required = false)
            @Parameter(description = "조회할 년월", example = "2021-12")
            String yearMonth
    );
}
