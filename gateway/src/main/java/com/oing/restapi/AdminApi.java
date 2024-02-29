package com.oing.restapi;

import com.oing.dto.response.AdminDailyDashboardResponse;
import com.oing.dto.response.AdminDashboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "관리자 API", description = "관리자용 API")
@RestController
@RequestMapping("/v1/admin")
public interface AdminApi {
    @Operation(summary = "기본 대시보드 값 조회", description = "기본 대시보드 수치를 조회합니다")
    @GetMapping("/dashboard")
    AdminDashboardResponse getDashboard();

    @Operation(summary = "대시보드 일별 수치 조회", description = "일별 대시보드 수치를 조회합니다")
    @GetMapping(value = "/dashboard/daily", params = {"fromDate", "toDate"})
    AdminDailyDashboardResponse getDailyDashboard(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "조회 시작 일자", example = "2024-02-19")
            LocalDate fromDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "조회 마지막 일자", example = "2024-02-21")
            LocalDate toDate
    );
}
