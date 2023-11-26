package com.oing.config.support;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3PreSignedUrlProvider {

    @Value("${cloud.ncp.storage.bucket}")
    private String bucket;

    private final String prefixImagePath = "images";

    private final AmazonS3Client amazonS3Client;

    public PreSignedUrlResponse getPreSignedUrl(String imageName, Long memberId) {

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(imageName, memberId);

        return new PreSignedUrlResponse(generatePreSignedUrl(generatePresignedUrlRequest));
    }

    private String generatePreSignedUrl(GeneratePresignedUrlRequest generatePresignedUrlRequest) {

        String preSignedUrl;
        try {
            preSignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } catch (AmazonServiceException e) {
            log.error("Pre-signed Url 생성 실패했습니다. {}", e.getMessage());
            throw new IllegalStateException("Pre-signed Url 생성 실패했습니다.");
        }
        return preSignedUrl;
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String imageName, Long memberId) {
        String savedImageName = uniqueImageName(imageName, memberId);

        String savedImagePath = savedImageName;
        if (!prefixImagePath.isBlank()) {
            savedImagePath = prefixImagePath + "/" + savedImageName;
        }
        return getPreSignedUrlRequest(bucket, savedImagePath);
    }

    private GeneratePresignedUrlRequest getPreSignedUrlRequest(String bucket, String imageName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, imageName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    /**
     * image path 형식 : "images" + "/" + memberId + "/" + UUID + "_" + imageName + 확장자;
     */
    private String uniqueImageName(String imageName, Long memberId) {
        return memberId + "/" + UUID.randomUUID() + "_" + imageName;
    }
}