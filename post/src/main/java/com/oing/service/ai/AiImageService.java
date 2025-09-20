package com.oing.service.ai;

import com.oing.dto.response.AiImageResponse;
import com.oing.util.ImageUploadUtil;
import com.oing.util.OpenAIImageTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiImageService {
    private final OpenAIImageTransformer openAIImageTransformer;
    private final ImageUploadUtil imageUploadUtil;

    /**
     * 이미지 변환
     */
    @Transactional
    public AiImageResponse convertImage(MultipartFile file, String loginMemberId) {
        try {
            // 1. 파일 바이트 읽기
            byte[] fileBytes = file.getBytes();

            // 2. 이미지 분석 → GPT 텍스트 생성
            String description = openAIImageTransformer.analyzeImageToText(fileBytes);
            log.info("GPT 이미지 분석 완료 - Member: {}, Description: {}", loginMemberId, description);

            // 3. 텍스트 기반 이미지 생성
            byte[] generatedImageBytes = openAIImageTransformer.generateImageFromText(description);
            log.info("DALL·E 이미지 생성 완료 - Member: {}", loginMemberId);

            // 4. 생성된 이미지 업로드
            String ncpImageUrl = imageUploadUtil.uploadImage(generatedImageBytes);
            log.info("AI 이미지 변환 완료 - Member: {}, 변환 이미지: {}", loginMemberId, ncpImageUrl);

            return new AiImageResponse(ncpImageUrl, file.getOriginalFilename());
        } catch (Exception e) {
            log.error("AI 이미지 변환 실패 - Member: {}, Error: {}", loginMemberId, e.getMessage(), e);
            throw new RuntimeException("AI 이미지 변환에 실패했습니다: " + e.getMessage(), e);
        }
    }
}
