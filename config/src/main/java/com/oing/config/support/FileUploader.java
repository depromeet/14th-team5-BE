package com.oing.config.support;

import com.amazonaws.services.s3.AmazonS3;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Component
public class FileUploader {

    private final AmazonS3 amazonS3Client;
    private final String bucket;
    private final String prefixImagePath = "images";


    public FileUploader(AmazonS3 amazonS3Client, @Value("${cloud.ncp.storage.bucket}") String bucket) {
        this.amazonS3Client = amazonS3Client;
        this.bucket = bucket;
    }

    public PreSignedUrlResponse getImageUploadPresignedUrl(
            final Long memberId,
            final PreSignedUrlRequest request
    ) {

        final String customKey = prefixImagePath + "/" + UUID.randomUUID() + memberId;
        final String url = s3PresignedUrlProvider.getImageUploadPresignedUrl(
                request.getCheckSum(), request.getContentLength(), customKey);

        return new PreSignedUrlResponse(url);
    }
}
