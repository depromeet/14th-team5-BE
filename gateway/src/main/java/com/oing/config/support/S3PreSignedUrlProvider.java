package com.oing.config.support;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3PreSignedUrlProvider implements PreSignedUrlGenerator {

    private final AmazonS3Client amazonS3Client;
    private final IdentityGenerator identityGenerator;
    @Value("${cloud.ncp.storage.bucket}")
    private String bucket;

    @Override
    public PreSignedUrlResponse getFeedPreSignedUrl(String imageName) {

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGenerateImagePreSignedUrlRequest("feed",
                imageName);

        return new PreSignedUrlResponse(generatePreSignedUrl(generatePresignedUrlRequest));
    }

    @Override
    public PreSignedUrlResponse getProfileImagePreSignedUrl(String imageName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGenerateImagePreSignedUrlRequest("profile",
                imageName);

        return new PreSignedUrlResponse(generatePreSignedUrl(generatePresignedUrlRequest));
    }

    @Override
    public PreSignedUrlResponse getRealEmojiPreSignedUrl(String imageName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGenerateImagePreSignedUrlRequest("real-emoji",
                imageName);

        return new PreSignedUrlResponse(generatePreSignedUrl(generatePresignedUrlRequest));
    }

    @Override
    public PreSignedUrlResponse getVoiceCommentPreSignedUrl(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGenerateAudioPreSignedUrlRequest("voice-comment",
                fileName);

        return new PreSignedUrlResponse(generatePreSignedUrl(generatePresignedUrlRequest));
    }

    @Override
    public PreSignedUrlResponse getOriginalImagePreSignedUrl(String imageName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGenerateAudioPreSignedUrlRequest("ai-image/original",
                imageName);

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

    private GeneratePresignedUrlRequest getGenerateImagePreSignedUrlRequest(String directory, String imageName) {
        String savedImageName = generateUniqueImageName(imageName);
        String savedImagePath = "images" + "/" + directory + "/" + savedImageName;

        return getPreSignedUrlRequest(bucket, savedImagePath);
    }

    private GeneratePresignedUrlRequest getGenerateAudioPreSignedUrlRequest(String directory, String fileName) {
        String savedFileName = generateUniqueImageName(fileName);
        String savedFilePath = "audios" + "/" + directory + "/" + savedFileName;

        return getPreSignedUrlRequest(bucket, savedFilePath);
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
     * image path 형식 : "images" + "/" + "기능 dir" + "/" + ULID + 확장자;
     */
    private String generateUniqueImageName(String imageName) {
        String ext = imageName.substring(imageName.lastIndexOf("."));
        return identityGenerator.generateIdentity() + ext;
    }

    @Override
    public String extractImageKey(String imageUrl) {
        if (imageUrl == null) {
            return null;
        }
        Pattern pattern1 = Pattern.compile("https://.+?/" + Pattern.quote(bucket) + "/(.+)");
        Pattern pattern2 = Pattern.compile("https://" + Pattern.quote(bucket) + ".+?/" + "(.+)");

        Matcher matcher = pattern1.matcher(imageUrl);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        matcher.usePattern(pattern2);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        throw new InvalidParameterException();
    }
}
