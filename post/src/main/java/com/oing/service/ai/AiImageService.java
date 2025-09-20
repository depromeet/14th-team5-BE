package com.oing.service.ai;

import com.oing.dto.request.CreateAiImageRequest;
import com.oing.dto.response.AiImageResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.util.ImageUploadUtil;
import com.oing.util.OpenAIImageGenerator;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiImageService {
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final OpenAIImageGenerator openAIImageGenerator;
    private final ImageUploadUtil imageUploadUtil;

    @Transactional
    public PreSignedUrlResponse requestPresignedUrl(String loginMemberId, String imageName) {
        log.info("Member {} is trying to request Ai Image Pre-Signed URL", loginMemberId);

        PreSignedUrlResponse response = preSignedUrlGenerator.getOriginalImagePreSignedUrl(imageName);
        log.info("Ai Image Pre-Signed URL has been generated for member {}: {}", loginMemberId, response.url());
        return response;
    }

    @Transactional
    public AiImageResponse convertImage(CreateAiImageRequest request, String loginMemberId) {
        log.info("Member {} is trying to convert image: {}", loginMemberId, request.imageUrl());

        try {
            // 1. OpenAI API로 이미지 변환
            String generatedImageUrl = openAIImageGenerator.generateHanbokImage(request.imageUrl());

            // 2. 생성된 이미지를 NCP에 업로드
            String ncpImageUrl = imageUploadUtil.uploadImage(generatedImageUrl);

            log.info("AI 이미지 변환 완료 - Member: {}, 원본: {}, 변환: {}",
                    loginMemberId, request.imageUrl(), ncpImageUrl);

            return new AiImageResponse(ncpImageUrl, request.imageUrl());

        } catch (Exception e) {
            log.error("AI 이미지 변환 실패 - Member: {}, Error: {}", loginMemberId, e.getMessage(), e);
            throw new RuntimeException("AI 이미지 변환에 실패했습니다: " + e.getMessage(), e);
        }
    }
}
