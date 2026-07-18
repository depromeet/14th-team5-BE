package com.oing.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.oing.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyPhotoDownloadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.ncp.storage.bucket}")
    private String bucket;

    public StreamingResponseBody createFamilyPhotoZipStream(List<Post> posts) {
        return outputStream -> {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
                for (Post post : posts) {
                    writePostImage(zipOutputStream, post);
                }
            }
        };
    }

    private void writePostImage(ZipOutputStream zipOutputStream, Post post) throws IOException {
        String imageKey = post.getPostImgKey();
        if (imageKey == null || imageKey.isBlank()) {
            log.warn("이미지 키가 없어 가족 사진 ZIP에서 제외합니다. postId: {}", post.getId());
            return;
        }

        S3Object s3Object;
        try {
            s3Object = amazonS3Client.getObject(bucket, imageKey);
        } catch (SdkClientException e) {
            log.warn("이미지 다운로드에 실패해 가족 사진 ZIP에서 제외합니다. postId: {}, imageKey: {}, cause: {}",
                    post.getId(), imageKey, e.getMessage());
            return;
        }

        try (s3Object; InputStream imageInputStream = s3Object.getObjectContent()) {
            zipOutputStream.putNextEntry(new ZipEntry(generateEntryName(post, imageKey)));
            imageInputStream.transferTo(zipOutputStream);
            zipOutputStream.closeEntry();
        }
    }

    /**
     * zip entry 형식 : 업로드일자_postId + 확장자 (ex. 2026-07-18_01H1VYYYPV6JT4DPGE0TDCB6P6.jpg)
     */
    private String generateEntryName(Post post, String imageKey) {
        String ext = imageKey.contains(".") ? imageKey.substring(imageKey.lastIndexOf(".")) : "";
        return String.format("%s_%s%s", post.getCreatedAt().toLocalDate(), post.getId(), ext);
    }
}
