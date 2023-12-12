package com.oing.restapi;

import com.oing.dto.response.ArrayResponse;
import com.oing.dto.response.CalendarResponse;
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
    @Operation(summary = "주별 캘린더 조회", description = "주별 캘린더를 조회합니다.")
    @GetMapping(params = {"type=WEEKLY"})
    ArrayResponse<CalendarResponse> getWeeklyCalendar(
            @RequestParam(required = false)
            @Parameter(description = "조회할 년월", example = "2021-12")
            String yearMonth,

            @RequestParam(required = false)
            @Parameter(description = "조회할 주차", example = "1")
            Integer week
    );

    @Operation(summary = "월별 캘린더 조회", description = "월별 캘린더를 조회합니다.")
    @GetMapping(params = {"type=MONTHLY"})
    ArrayResponse<CalendarResponse> getMonthlyCalendar(
            @RequestParam(required = false)
            @Parameter(description = "조회할 년월", example = "2021-12")
            String yearMonth
    );
}
