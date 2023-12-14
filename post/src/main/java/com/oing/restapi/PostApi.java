package com.oing.restapi;

import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import com.oing.dto.response.PostResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/image-upload-request")
    PreSignedUrlResponse requestPresignedUrl(
            @Parameter(description = "회원 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E")
            Long memberId,

            @Parameter(description = "이미지 이름", example = "image")
            String imageName
    );

    @Operation(summary = "게시물 조회", description = "게시물 목록을 조회합니다. 조회 기준은 생성 순서입니다.")
    @GetMapping
    PaginationResponse<PostResponse> fetchDailyFeeds(
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
            @Parameter(description = "조회 대상 날짜", example = "2023-12-05")
            LocalDate date,

            @RequestParam(required = false)
            @Parameter(description = "대상 사용자 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            String memberId,

            @RequestParam(required = false)
            @Parameter(description = "정렬 방식", example = "DESC | ASC")
            String sort
    );

    @Operation(summary = "게시물 생성", description = "게시물을 생성합니다.")
    @PostMapping
    PostResponse createPost(
            @RequestBody
            CreatePostRequest request
    );

    @Operation(summary = "단일 게시물 조회", description = "ID를 통해 게시물을 조회합니다.")
    @GetMapping("/{postId}")
    PostResponse getPost(
            @PathVariable
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            String postId
    );
}
