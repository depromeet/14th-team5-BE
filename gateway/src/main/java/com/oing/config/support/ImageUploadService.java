package com.oing.config.support;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.oing.util.IdentityGenerator;
import com.oing.util.ImageUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageUploadService implements ImageUploadUtil {

    @Value("${cloud.ncp.storage.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    private final IdentityGenerator identityGenerator;

    @Override
    public String uploadImage(byte[] imageBytes) {
        String uniqueImageName = identityGenerator.generateIdentity() + ".png";
        String imagePath = "images/ai/generated/" + uniqueImageName;

        try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            metadata.setContentLength(imageBytes.length);

            amazonS3Client.putObject(new PutObjectRequest(bucket, imagePath, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return buildObjectStorageUrl(imagePath);

        } catch (Exception e) {
            log.error("이미지 업로드 실패: {}", e.getMessage(), e);
            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage(), e);
        }
    }

    /**
     * NCP Object Storage URL 생성
     * 기존 S3PreSignedUrlProvider의 패턴을 따라서 URL 생성
     */
    private String buildObjectStorageUrl(String imagePath) {
        return String.format("https://%s.kr.object.ncloudstorage.com/%s", bucket, imagePath);
    }
}
