package com.oing.controller;


import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
import com.oing.domain.model.MemberPost;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.restapi.PostApi;
import com.oing.service.MemberPostService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:24 PM
 */
@RequiredArgsConstructor
@Controller
public class PostController implements PostApi {

    private final AuthenticationHolder authenticationHolder;
    private final IdentityGenerator identityGenerator;
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final MemberPostService memberPostService;

    @Override
    public PreSignedUrlResponse requestPresignedUrl(String imageName) {
        return preSignedUrlGenerator.getFeedPreSignedUrl(imageName);
    }

    @Override
    public PaginationResponse<PostResponse> fetchDailyFeeds(Integer page, Integer size, LocalDate date, String memberId, String sort) {
        if (page > 5) return new PaginationResponse<>(page, 5, size, false, List.of());
        if(memberId != null) {
            return new PaginationResponse<>(page, 5, size, false, List.of(
                    new PostResponse(
                            "01HGW2N7EHJVJ4CJ999RRS2E",
                            memberId,
                            0,
                            0,
                            "https://picsum.photos/200/300?random=00",
                            "즐거운 하루~",
                            ZonedDateTime.now()
                    )
            ));
        }

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

    @Transactional
    @Override
    public PostResponse createPost(CreatePostRequest request) {
        String myId = authenticationHolder.getUserId();
        String postId = identityGenerator.generateIdentity();
        ZonedDateTime uploadTime = ZonedDateTime.parse(request.uploadTime());

        validateUserHasNotCreatedPostToday(myId, uploadTime);
        validateUploadTime(uploadTime);

        LocalDate uploadDate = uploadTime.toLocalDate();
        MemberPost post = new MemberPost(postId, myId, uploadDate, request.imageUrl(), request.content());
        memberPostService.save(post);

        return new PostResponse(postId, myId, 0, 0, request.imageUrl(), request.content(),
                uploadTime);
    }

    private void validateUserHasNotCreatedPostToday(String memberId, ZonedDateTime uploadTime) {
        LocalDate today = uploadTime.toLocalDate();
        if (memberPostService.hasUserCreatedPostToday(memberId, today)) {
            throw new DomainException(ErrorCode.INVALID_UPLOAD_TIME);
        }
    }

    private void validateUploadTime(ZonedDateTime uploadTime) {
        ZonedDateTime serverTime = ZonedDateTime.now();
        ZonedDateTime lowerBound = serverTime.with(LocalTime.of(12, 0)).minusDays(serverTime.getHour() < 12 ? 1 : 0);
        ZonedDateTime upperBound = lowerBound.plusDays(1);

        if (uploadTime.isBefore(lowerBound) || uploadTime.isAfter(upperBound)) {
            throw new DomainException(ErrorCode.INVALID_UPLOAD_TIME);
        }
    }

    @Override
    public PostResponse getPost(String postId) {
        return new PostResponse(
                postId,
                "01HGW2N7EHJVJ4CJ888RRS2E",
                0,
                0,
                "https://picsum.photos/200/300?random=00",
                "즐거운 하루~",
                ZonedDateTime.now()
        );
    }
}
