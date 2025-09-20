package com.oing.util;

import com.oing.dto.response.PreSignedUrlResponse;

public interface PreSignedUrlGenerator {
    PreSignedUrlResponse getFeedPreSignedUrl(String imageName);

    PreSignedUrlResponse getProfileImagePreSignedUrl(String imageName);

    PreSignedUrlResponse getRealEmojiPreSignedUrl(String imageName);

    PreSignedUrlResponse getVoiceCommentPreSignedUrl(String fileName);

    PreSignedUrlResponse getOriginalImagePreSignedUrl(String imageName);

    PreSignedUrlResponse getAiGeneratedImagePreSignedUrl(String imageName);

    String extractImageKey(String imageUrl);
}
