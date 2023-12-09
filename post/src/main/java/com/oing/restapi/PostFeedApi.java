package com.oing.restapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "피드 API", description = "게시물 피드 관련 API")
@RestController
@Valid
@RequestMapping("/v1/feeds")
public interface PostFeedApi {

    @Operation(summary = "금일 피드 업로드 여부 조회", description = "특정 유저의 금일 피드 업로드 여부를 boolean으로 반환합니다.")
    @GetMapping("{userId}/today/")
    ResponseEntity<Boolean> getIsTodayFeedUploadedByUserId(
            @RequestParam(value = "userId", required = true)
            @Parameter(description = "유저 아이디", example = "01HH60VYKCFAHQY21N8EFM8BED")
            String userId
    );
}
