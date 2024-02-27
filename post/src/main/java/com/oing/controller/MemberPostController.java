package com.oing.controller;


import com.oing.domain.MemberPost;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.DuplicatePostUploadException;
import com.oing.exception.InvalidUploadTimeException;
import com.oing.restapi.MemberPostApi;
import com.oing.service.MemberBridge;
import com.oing.service.MemberPostService;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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
@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberPostController implements MemberPostApi {

    private final IdentityGenerator identityGenerator;
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final MemberPostService memberPostService;
    private final MemberBridge memberBridge;

    @Transactional
    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request, String loginMemberId) {
        log.info("Member {} is trying to request post Pre-Signed URL", loginMemberId);
        String imageName = request.imageName();

        PreSignedUrlResponse response = preSignedUrlGenerator.getFeedPreSignedUrl(imageName);
        log.info("Post Pre-Signed URL has been generated for member {}: {}", loginMemberId, response.url());

        return response;
    }

    @Override
    public PaginationResponse<PostResponse> fetchDailyFeeds(Integer page, Integer size, LocalDate date, String memberId,
                                                            String sort, String loginMemberId) {
        String familyId = memberBridge.getFamilyIdByMemberId(loginMemberId);
        PaginationDTO<MemberPost> fetchResult = memberPostService.searchMemberPost(
                page, size, date, memberId, loginMemberId, familyId, sort == null || sort.equalsIgnoreCase("ASC")
        );

        return PaginationResponse
                .of(fetchResult, page, size)
                .map(PostResponse::from);
    }

    @Transactional
    @Override
    @CacheEvict(value = "calendarCache",
            key = "#loginFamilyId.concat(':').concat(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM').format(#request.uploadTime()))")
    public PostResponse createPost(CreatePostRequest request, String loginFamilyId, String loginMemberId) {
        log.info("Member {} is trying to create post", loginMemberId);
        String postId = identityGenerator.generateIdentity();
        ZonedDateTime uploadTime = request.uploadTime();

        validateUserHasNotCreatedPostToday(loginMemberId, loginFamilyId, uploadTime);
        validateUploadTime(loginMemberId, uploadTime);

        String postImgKey = preSignedUrlGenerator.extractImageKey(request.imageUrl());
        MemberPost post = new MemberPost(postId, loginMemberId, loginFamilyId, request.imageUrl(),
                postImgKey, request.content());
        MemberPost savedPost = memberPostService.save(post);
        log.info("Member {} has created post {}", loginMemberId, postId);

        return PostResponse.from(savedPost);
    }

    private void validateUserHasNotCreatedPostToday(String memberId, String familyId, ZonedDateTime uploadTime) {
        LocalDate today = uploadTime.toLocalDate();
        if (memberPostService.hasUserCreatedPostToday(memberId, familyId, today)) {
            log.warn("Member {} has already created a post today", memberId);
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
    private void validateUploadTime(String memberId, ZonedDateTime uploadTime) {
        ZonedDateTime serverTime = ZonedDateTime.now();

        ZonedDateTime lowerBound = serverTime.minusDays(1).with(LocalTime.of(12, 0));
        ZonedDateTime upperBound = serverTime.plusDays(1).with(LocalTime.of(12, 0));

        if (uploadTime.isBefore(lowerBound) || uploadTime.isAfter(upperBound)) {
            log.warn("Member {} is attempting to upload a post at an invalid time", memberId);
            throw new InvalidUploadTimeException();
        }
    }

    @Override
    public PostResponse getPost(String postId, String loginMemberId) {
        String postFamilyId = memberPostService.getMemberPostById(postId).getFamilyId();
        String loginFamilyId = memberBridge.getFamilyIdByMemberId(loginMemberId);
        if (!postFamilyId.equals(loginFamilyId)) {
            log.warn("Unauthorized access attempt: Member {} is attempting to access post {}", loginMemberId, postId);
            throw new AuthorizationFailedException();
        }

        MemberPost memberPostProjection = memberPostService.getMemberPostById(postId);
        return PostResponse.from(memberPostProjection);
    }
}
