package com.oing.restapi;

import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AI 이미지 API", description = "AI 이미지 관련 API")
@RestController
@Valid
@RequestMapping("/v1/ai-images")
public interface AiImageApi {

    @Operation(summary = "AI 원본 사진 S3 Presigned Url 요청", description = "S3 Presigned Url을 요청합니다.")
    @PostMapping("/image-upload-request")
    PreSignedUrlResponse requestPresignedUrl(
            @Valid
            @RequestBody
            PreSignedUrlRequest request,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );
}
