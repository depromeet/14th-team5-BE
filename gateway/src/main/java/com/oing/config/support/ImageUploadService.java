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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageUploadService implements ImageUploadUtil {

    @Value("${cloud.ncp.storage.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    private final IdentityGenerator identityGenerator;

    @Override
    public String uploadImage(String generatedImageUrl) {
        String uniqueImageName = identityGenerator.generateIdentity() + ".png";
        String imagePath = "images/ai-image/generated/" + uniqueImageName;

        try {
            log.info("Generated image upload 시작 - URL: {}", generatedImageUrl);

            URL url = new URL(generatedImageUrl);
            URLConnection connection = url.openConnection();

            try (InputStream inputStream = connection.getInputStream()) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType("image/png");
                if (connection.getContentLength() > 0) {
                    metadata.setContentLength(connection.getContentLength());
                }

                PutObjectRequest putObjectRequest = new PutObjectRequest(
                        bucket,
                        imagePath,
                        inputStream,
                        metadata
                ).withCannedAcl(CannedAccessControlList.PublicRead);

                amazonS3Client.putObject(putObjectRequest);

                // NCP Object Storage URL 생성 (기존 패턴과 동일하게)
                String uploadedImageUrl = buildObjectStorageUrl(imagePath);

                log.info("Generated image upload 성공 - NCP URL: {}", uploadedImageUrl);
                return uploadedImageUrl;
            }

        } catch (IOException e) {
            log.error("Generated image upload 실패: {}", e.getMessage(), e);
            throw new RuntimeException("이미지 업로드에 실패했습니다: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("이미지 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
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
