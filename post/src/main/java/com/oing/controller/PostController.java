package com.oing.controller;


import com.oing.domain.PaginationDTO;
import com.oing.domain.model.MemberPost;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.exception.InvalidUploadTimeException;
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
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request) {
        String imageName = request.imageName();
        return preSignedUrlGenerator.getFeedPreSignedUrl(imageName);
    }

    @Override
    public PaginationResponse<PostResponse> fetchDailyFeeds(Integer page, Integer size, LocalDate date, String memberId,
                                                            String sort) {
        PaginationDTO<MemberPost> fetchResult = memberPostService.searchMemberPost(
                page, size, date, memberId, sort == null || sort.equalsIgnoreCase("ASC")
        );

        return PaginationResponse
                .of(fetchResult, page, size)
                .map(post -> {
                    String convertedImageUrl = preSignedUrlGenerator.convertImageUrl(post.getImageUrl());
                    return PostResponse.from(post, convertedImageUrl);
                });
    }

    @Transactional
    @Override
    public PostResponse createPost(CreatePostRequest request) {
        String memberId = authenticationHolder.getUserId();
        String postId = identityGenerator.generateIdentity();
        ZonedDateTime uploadTime = request.uploadTime();

        validateUserHasNotCreatedPostToday(memberId, uploadTime);
        validateUploadTime(uploadTime);

        LocalDate uploadDate = uploadTime.toLocalDate();
        MemberPost post = new MemberPost(postId, memberId, uploadDate, request.imageUrl(), request.content());
        memberPostService.save(post);
        String convertedImageUrl = preSignedUrlGenerator.convertImageUrl(request.imageUrl());

        return new PostResponse(postId, memberId, 0, 0, convertedImageUrl, request.content(),
                uploadTime);
    }

    private void validateUserHasNotCreatedPostToday(String memberId, ZonedDateTime uploadTime) {
        LocalDate today = uploadTime.toLocalDate();
        if (memberPostService.hasUserCreatedPostToday(memberId, today)) {
            throw new InvalidUploadTimeException();
        }
    }

    private void validateUploadTime(ZonedDateTime uploadTime) {
        ZonedDateTime serverTime = ZonedDateTime.now();

        ZonedDateTime lowerBound = serverTime.minusDays(1).with(LocalTime.of(12, 0));
        ZonedDateTime upperBound = serverTime.plusDays(1).with(LocalTime.of(12, 0));

        if (uploadTime.isBefore(lowerBound) || uploadTime.isAfter(upperBound)) {
            throw new InvalidUploadTimeException();
        }
    }

    @Override
    public PostResponse getPost(String postId) {
        MemberPost memberPostProjection = memberPostService.getMemberPostById(postId);
        String convertedImageUrl = preSignedUrlGenerator.convertImageUrl(memberPostProjection.getImageUrl());
        return PostResponse.from(memberPostProjection, convertedImageUrl);
    }
}
