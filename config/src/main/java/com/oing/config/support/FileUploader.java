package com.oing.config.support;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.oing.config.properties.ObjectStorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploader {

    private final AmazonS3 amazonS3Client;

    private final ObjectStorageProperties properties;

    private static final String OBJECT_STORAGE_BUCKET_DIRECTORY_NAME = "static";

    public String uploadImage(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String fileName = OBJECT_STORAGE_BUCKET_DIRECTORY_NAME + "/" + UUID.randomUUID() + "."
                + multipartFile.getOriginalFilename();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(properties.getBucketName(), fileName, inputStream,
                    objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.error("ObjectStorage 파일 업로드에 실패했습니다. {}", e.getMessage());
            throw new IllegalStateException("ObjectStorage 파일 업로드에 실패했습니다.");
        }
        return amazonS3Client.getUrl(properties.getBucketName(), fileName).toString();
    }
}
