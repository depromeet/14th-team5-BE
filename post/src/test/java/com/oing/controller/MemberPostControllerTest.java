package com.oing.controller;

import com.oing.domain.MemberPost;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.service.MemberBridge;
import com.oing.service.MemberPostService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberPostControllerTest {
    @InjectMocks
    private MemberPostController memberPostController;

    @Mock
    private AuthenticationHolder authenticationHolder;
    @Mock
    private IdentityGenerator identityGenerator;
    @Mock
    private MemberPostService memberPostService;
    @Mock
    private MemberBridge memberBridge;
    @Mock
    private PreSignedUrlGenerator preSignedUrlGenerator;

    @Test
    void 피드_이미지_업로드_URL_요청_테스트() {
        // given
        String newFeedImage = "feed.jpg";

        // when
        PreSignedUrlRequest request = new PreSignedUrlRequest(newFeedImage);
        PreSignedUrlResponse dummyResponse = new PreSignedUrlResponse("https://test.com/presigend-request-url");
        when(preSignedUrlGenerator.getFeedPreSignedUrl(any())).thenReturn(dummyResponse);
        PreSignedUrlResponse response = memberPostController.requestPresignedUrl(request);

        // then
        assertNotNull(response.url());
    }
}
