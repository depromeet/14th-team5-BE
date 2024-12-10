package com.oing.service;

import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoiceCommentService {

    private final IdentityGenerator identityGenerator;
    private final PreSignedUrlGenerator preSignedUrlGenerator;

    @Transactional
    public PreSignedUrlResponse requestPresignedUrl(String loginMemberId, String fileName) {
        log.info("Member {} is trying to request VoiceComment Pre-Signed URL", loginMemberId);

        PreSignedUrlResponse response = preSignedUrlGenerator.getFeedPreSignedUrl(fileName);
        log.info("VoiceComment Pre-Signed URL has been generated for member {}: {}", loginMemberId, response.url());
        return response;
    }
}
