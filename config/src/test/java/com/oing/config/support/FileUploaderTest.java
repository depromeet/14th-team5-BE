package com.oing.config.support;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.oing.support.InfraTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import static com.oing.utils.FileMockingUtils.createSingleMockMultipartFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class FileUploaderTest extends InfraTest {

    private FileUploader uploader;

    @Mock
    private AmazonS3 amazonS3;

    private static final String BUCKET = "bucket";
    private static final String IMAGE = "images";

    @BeforeEach
    void setUp() {
        uploader = new FileUploader(amazonS3, BUCKET);
    }

    @Test
    @DisplayName("이미지 업로드를 성공한다")
    void uploadImage() throws IOException {
        // given
        MultipartFile file = createSingleMockMultipartFile("test_img.jpg", "image/jpg");
        PutObjectResult putObjectResult = new PutObjectResult();
        given(amazonS3.putObject(any(PutObjectRequest.class))).willReturn(putObjectResult);
        URL mockUrl = new URL(createUploadLink());
        given(amazonS3.getUrl(eq(BUCKET), anyString())).willReturn(mockUrl);

        // when
        String uploadUrl = uploader.uploadImage(file);

        // then
        verify(amazonS3, times(1)).putObject(any(PutObjectRequest.class));
        verify(amazonS3, times(1)).getUrl(eq(BUCKET), anyString());
        assertThat(uploadUrl).isEqualTo(mockUrl.toString());
    }

    private String createUploadLink() {
        return String.format(
                "https://kr.object.ncloudstorage.com/%s/%s/%s-%s",
                BUCKET,
                FileUploaderTest.IMAGE,
                UUID.randomUUID(),
                "test_img.jpg"
        );
    }
}
