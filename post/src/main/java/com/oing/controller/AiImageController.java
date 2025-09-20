package com.oing.controller;

import com.oing.dto.response.AiImageResponse;
import com.oing.restapi.AiImageApi;
import com.oing.service.ai.AiImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AiImageController implements AiImageApi {
    private final AiImageService aiImageService;

    @Override
    public AiImageResponse convertImage(MultipartFile image, String loginMemberId) {
        try {
            return aiImageService.convertImage(image, loginMemberId);
        } catch (RuntimeException e) {
            log.error("AI 이미지 변환 실패 - Member: {}, Error: {}", loginMemberId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생 - Member: {}", loginMemberId, e);
            throw new RuntimeException("서버 내부 오류가 발생했습니다.", e);
        }
    }
}
