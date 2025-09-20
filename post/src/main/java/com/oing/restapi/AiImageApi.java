package com.oing.restapi;

import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.AiImageResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "AI 이미지 API", description = "AI 이미지 관련 API")
@RestController
@Valid
@RequestMapping("/v1/ai-images")
public interface AiImageApi {

    @Operation(summary = "AI 이미지 변환 요청", description = "원본 이미지를 한복 입은 추석 분위기로 변환합니다.")
    @PostMapping("/convert")
    AiImageResponse convertImage(
            @RequestParam("image") MultipartFile image,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );
}
