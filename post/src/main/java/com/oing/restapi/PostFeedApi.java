package com.oing.restapi;

import com.oing.dto.response.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import com.oing.dto.response.PostFeedResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:23 PM
 */
@Tag(name = "피드 API", description = "게시물 피드 관련 API")
@RestController
@Valid
@RequestMapping("/v1/feeds")
public interface PostFeedApi {
    @Operation(summary = "데일리 피드 조회", description = "데일리 피드를 조회합니다.")
    @GetMapping(params = {"type=DAILY"})
    PaginationResponse<PostFeedResponse> fetchDailyFeeds(
            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "가져올 현재 페이지", example = "1")
            @Min(value = 1)
            Integer page,

            @RequestParam(required = false, defaultValue = "10")
            @Parameter(description = "가져올 페이지당 크기", example = "10")
            @Min(value = 1)
            Integer size,

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam(required = false)
            @Parameter(description = "오늘의 날짜", example = "2023-12-05")
            LocalDate date
    );
}
