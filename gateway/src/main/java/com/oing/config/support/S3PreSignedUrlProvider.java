package com.oing.config.support;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.util.PreSignedUrlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3PreSignedUrlProvider implements PreSignedUrlGenerator {

    @Value("${cloud.ncp.storage.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    @Override
    public PreSignedUrlResponse getFeedPreSignedUrl(String imageName, Long memberId) {

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest("feed",
                imageName, memberId);

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

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String directory, String imageName, Long memberId) {
        String savedImageName = uniqueImageName(imageName, memberId);
        String savedImagePath = "images" + "/" + directory + "/" + savedImageName;

        return getPreSignedUrlRequest(bucket, savedImagePath);
    }

    private GeneratePresignedUrlRequest getPreSignedUrlRequest(String bucket, String imageName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, imageName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(new Date(System.currentTimeMillis() + 180000));

        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    /**
     * image path 형식 : "images" + "/" + memberId + "/" + UUID + "_" + imageName + 확장자;
     */
    private String uniqueImageName(String imageName, Long memberId) {
        return memberId + "/" + UUID.randomUUID() + "_" + imageName;
    }
}
