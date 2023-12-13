package com.oing.config.support;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.support.InfraTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class S3PreSignedUrlProviderTest extends InfraTest {

    private S3PreSignedUrlProvider provider;

    @Mock
    private AmazonS3Client amazonS3Client;

    @BeforeEach
    void setUp() {
        provider = new S3PreSignedUrlProvider(amazonS3Client);
    }

    @Test
    @DisplayName("PreSigned Url을 성공적으로 얻는다")
    void getPreSignedUrl() {
        //given
        String imageName = "test-image";
        Long memberId = 123L;
        URL mockPresignedUrl = mock(URL.class);
        when(amazonS3Client.generatePresignedUrl(any(GeneratePresignedUrlRequest.class))).thenReturn(mockPresignedUrl);

        // when
        PreSignedUrlResponse response = provider.getFeedPreSignedUrl(imageName);

        // then
        Assertions.assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(mockPresignedUrl.toString(), response.getUrl())
        );
    }
}
