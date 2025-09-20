package com.oing.util;

public interface OpenAIImageGenerator {
    /**
     * 원본 이미지를 한복 입은 추석 분위기로 변환
     * @param originalImageUrl 원본 이미지 URL
     * @return 생성된 이미지 URL (OpenAI에서 제공하는 임시 URL)
     */
    String generateHanbokImage(String originalImageUrl);
}
