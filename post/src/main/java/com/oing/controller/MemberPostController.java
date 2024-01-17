package com.oing.controller;


import com.oing.domain.MemberPost;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.exception.DuplicatePostUploadException;
import com.oing.exception.InvalidUploadTimeException;
import com.oing.restapi.MemberPostApi;
import com.oing.service.MemberBridge;
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
public class MemberPostController implements MemberPostApi {

    private final AuthenticationHolder authenticationHolder;
    private final IdentityGenerator identityGenerator;
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final MemberPostService memberPostService;
    private final MemberBridge memberBridge;

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request) {
        String imageName = request.imageName();
        return preSignedUrlGenerator.getFeedPreSignedUrl(imageName);
    }

    @Override
    public PaginationResponse<PostResponse> fetchDailyFeeds(Integer page, Integer size, LocalDate date, String memberId, String sort) {
        String requesterMemberId = authenticationHolder.getUserId();
        String familyId = memberBridge.getFamilyIdByMemberId(requesterMemberId);
        PaginationDTO<MemberPost> fetchResult = memberPostService.searchMemberPost(
                page, size, date, memberId, requesterMemberId, familyId, sort == null || sort.equalsIgnoreCase("ASC")
        );

        return PaginationResponse
                .of(fetchResult, page, size)
                .map(PostResponse::from);
    }

    @Transactional
    @Override
    public PostResponse createPost(CreatePostRequest request) {
        String memberId = authenticationHolder.getUserId();
        String postId = identityGenerator.generateIdentity();
        ZonedDateTime uploadTime = request.uploadTime();

        validateUserHasNotCreatedPostToday(memberId, uploadTime);
        validateUploadTime(uploadTime);

        String postImgKey = preSignedUrlGenerator.extractImageKey(request.imageUrl());
        MemberPost post = new MemberPost(postId, memberId, request.imageUrl(),
                postImgKey, request.content());
        MemberPost savedPost = memberPostService.save(post);

        return PostResponse.from(savedPost);
    }

    private void validateUserHasNotCreatedPostToday(String memberId, ZonedDateTime uploadTime) {
        LocalDate today = uploadTime.toLocalDate();
        if (memberPostService.hasUserCreatedPostToday(memberId, today)) {
            throw new DuplicatePostUploadException();
        }
    }

    /**
     * 업로드 시간이 허용 가능한 범위 내에 있는지 검증합니다.
     * 범위는 서버의 로컬 시간을 기준으로 전 날의 오후 12시부터 다음 날의 오후 12시까지로 정의됩니다.
     *
     * @param uploadTime 검증할 업로드 시간입니다.
     * @throws InvalidUploadTimeException 업로드 시간이 허용 가능한 범위를 벗어난 경우 발생하는 예외입니다.
     */
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
        return PostResponse.from(memberPostProjection);
    }

    @Override
    public DefaultResponse deletePost(String postId) {
        memberPostService.deleteMemberPostById(postId);
        return DefaultResponse.ok();
    }
}
