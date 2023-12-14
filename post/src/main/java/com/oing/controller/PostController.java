package com.oing.controller;


import com.oing.domain.model.MemberPost;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.restapi.PostApi;
import com.oing.util.PreSignedUrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:24 PM
 */
@RequiredArgsConstructor
@Controller
public class PostController implements PostApi {

    private final PreSignedUrlGenerator preSignedUrlGenerator;

    @Override
    public ResponseEntity<PreSignedUrlResponse> requestPresignedUrl(String imageName) {
        return ResponseEntity.ok(preSignedUrlGenerator.getFeedPreSignedUrl(imageName));
    }

    @Override
    public PaginationResponse<PostResponse> fetchDailyFeeds(Integer page, Integer size, LocalDate date) {
        if (page > 5) return new PaginationResponse<>(page, 5, size, false, List.of());

        String postIdBase = "01HGW2N7EHJVJ4CJ999RRS2E";
        String writerIdBase = "01HGW2N7EHJVJ4CJ888RRS2E";

        List<PostResponse> mockResponses = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < size; i++) {
            int currentIndex = i + ((page - 1) * size);
            String suffix = String.format("%02d", currentIndex);
            mockResponses.add(
                    new PostResponse(
                            postIdBase + suffix,
                            writerIdBase + suffix,
                            random.nextInt(5),
                            random.nextInt(5),
                            "https://picsum.photos/200/300?random=" + currentIndex,
                            "hi",
                            ZonedDateTime.now().minusSeconds(currentIndex * 30L)
                    )
            );
        }

        return new PaginationResponse<>(page, 5, size, 5 > page, mockResponses);
    }

    @Override
    public ResponseEntity<PostResponse> fetchDailyFeeds() {
//        Optional<MemberPost> myPost = memberPostService.findPostByMemberId(tokenAuthenticationHolder.getUserId());
        Optional<MemberPost> myPost;
        if (new Random().nextBoolean()) {
            myPost = Optional.of(new MemberPost("01HGW2N7EHJVJ4CJ999RRS2E", "01HGW2N7EHJVJ4CJ888RRS2E",
                    LocalDate.now(), "https://picsum.photos/200/300?random=00", "hi",
                    0, 0));
        } else {
            myPost = Optional.empty();
        }

        if (myPost.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        String postIdBase = "01HGW2N7EHJVJ4CJ999RRS2E";
        String writerIdBase = "01HGW2N7EHJVJ4CJ888RRS2E";
        PostResponse mockResponse = new PostResponse(
                postIdBase,
                writerIdBase,
                0,
                0,
                "https://picsum.photos/200/300?random=00",
                "hi",
                ZonedDateTime.now()
        );

        return ResponseEntity.ok(mockResponse);
    }

    @Override
    public ResponseEntity<PostResponse> createPost(CreatePostRequest request) {
        String postIdBase = "01HGW2N7EHJVJ4CJ999RRS2E";
        String writerIdBase = "01HGW2N7EHJVJ4CJ888RRS2E";
        PostResponse mockResponse = new PostResponse(
                postIdBase,
                writerIdBase,
                0,
                0,
                request.imageUrl(),
                request.content(),
                ZonedDateTime.now()
        );

        return ResponseEntity.ok(mockResponse);
    }

    @Override
    public PostResponse getPost(String postId) {
        return null;
    }

    @Override
    public PaginationResponse<PostResponse> getPostsByMember(String memberId) {
        return null;
    }
}
