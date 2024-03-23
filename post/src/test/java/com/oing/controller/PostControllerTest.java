package com.oing.controller;

import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.service.MemberBridge;
import com.oing.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    @InjectMocks
    private PostController memberPostController;

    @Mock
    private PostService postService;
    @Mock
    private MemberBridge memberBridge;

    @Test
    void 피드_이미지_업로드_URL_요청_테스트() {
        // given
        String memberId = "1";
        String feedImage = "feed.jpg";

        // when
        PreSignedUrlRequest request = new PreSignedUrlRequest(feedImage);
        PreSignedUrlResponse dummyResponse = new PreSignedUrlResponse("https://test.com/presigend-request-url.jpg");
        when(postService.requestPresignedUrl(memberId, feedImage)).thenReturn(dummyResponse);
        PreSignedUrlResponse response = memberPostController.requestPresignedUrl(request, memberId);

        // then
        Assertions.assertNotNull(response.url());
    }
}
