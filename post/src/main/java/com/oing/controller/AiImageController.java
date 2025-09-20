package com.oing.controller;

import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.restapi.AiImageApi;
import com.oing.service.AiImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AiImageController implements AiImageApi {
    private final AiImageService aiImageService;

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request, String loginMemberId) {
        return aiImageService.requestPresignedUrl(loginMemberId, request.imageName());
    }
}
