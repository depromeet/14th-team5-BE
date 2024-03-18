package com.oing.config.support;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.util.IdentityGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3PreSignedUrlProviderTest {

    private S3PreSignedUrlProvider provider;

    @Mock
    private AmazonS3Client amazonS3Client;

    @BeforeEach
    void setUp() {
        IdentityGenerator identityGenerator = mock(IdentityGenerator.class);
        provider = new S3PreSignedUrlProvider(amazonS3Client, identityGenerator);
    }

    @Test
    @DisplayName("PreSigned Url을 성공적으로 얻는다")
    void getPreSignedUrl() {
        //given
        String imageName = "test_image.jpg";
        URL mockPresignedUrl = mock(URL.class);
        when(amazonS3Client.generatePresignedUrl(any(GeneratePresignedUrlRequest.class))).thenReturn(mockPresignedUrl);

        // when
        PreSignedUrlResponse response = provider.getFeedPreSignedUrl(imageName);

        // then
        Assertions.assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(mockPresignedUrl.toString(), response.url())
        );
    }
}
