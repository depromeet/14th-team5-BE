package com.oing.service;

import com.oing.dto.response.PreSignedUrlResponse;
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

    @Transactional
    public PreSignedUrlResponse requestPresignedUrl(String loginMemberId, String imageName) {
        log.info("Member {} is trying to request Ai Image Pre-Signed URL", loginMemberId);

        PreSignedUrlResponse response = preSignedUrlGenerator.getOriginalImagePreSignedUrl(imageName);
        log.info("Ai Image Pre-Signed URL has been generated for member {}: {}", loginMemberId, response.url());
        return response;
    }
}
