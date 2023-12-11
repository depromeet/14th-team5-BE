package com.oing.restapi;

import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostFeedResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "게시물 API", description = "게시물 관련 API")
@RestController
@Valid
@RequestMapping("/v1/posts")
public interface PostApi {
    @Operation(summary = "S3 Presigned Url 요청", description = "S3 Presigned Url을 요청합니다.")
    @GetMapping("/presigned-url")
    PreSignedUrlResponse requestPresignedUrl(
            @Parameter(description = "회원 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E")
            Long memberId,

            @Parameter(description = "이미지 이름", example = "image")
            String imageName
    );

    @Operation(summary = "데일리 게시물 조회", description = "오늘 가족들이 올린 게시물 목록을 조회합니다. (자신 제외)")
    @GetMapping(params = {"type=DAILY", "scope=FAMILY"})
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

    @Operation(summary = "데일리 게시물 조회", description = "오늘 자신이 올린 게시글을 조회합니다.\n 아직 업로드하지 않았으면 204를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "오늘 자신이 올린 게시글이 있는 경우")
    @ApiResponse(responseCode = "204", description = "오늘 자신이 올린 게시글이 없는 경우")
    @GetMapping(params = {"type=DAILY", "scope=ME"})
    ResponseEntity<PostFeedResponse> fetchDailyFeeds();
}
