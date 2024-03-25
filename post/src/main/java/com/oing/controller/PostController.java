package com.oing.controller;


import com.oing.domain.PaginationDTO;
import com.oing.domain.Post;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.restapi.PostApi;
import com.oing.service.MemberBridge;
import com.oing.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:24 PM
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class PostController implements PostApi {

    private final PostService postService;
    private final MemberBridge memberBridge;

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request, String loginMemberId) {
        return postService.requestPresignedUrl(loginMemberId, request.imageName());
    }

    @Override
    public PaginationResponse<PostResponse> fetchDailyFeeds(Integer page, Integer size, LocalDate date, String memberId,
                                                            String sort, String loginMemberId) {
        String familyId = memberBridge.getFamilyIdByMemberId(loginMemberId);
        PaginationDTO<Post> fetchResult = postService.searchMemberPost(
                page, size, date, memberId, loginMemberId, familyId, sort == null || sort.equalsIgnoreCase("ASC")
        );

        return PaginationResponse
                .of(fetchResult, page, size)
                .map(PostResponse::from);
    }

    @Override
    @CacheEvict(value = "calendarCache",
            key = "#loginFamilyId.concat(':').concat(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM').format(#request.uploadTime()))")
    public PostResponse createPost(CreatePostRequest request, String loginFamilyId, String loginMemberId) {
        log.info("Member {} is trying to create post", loginMemberId);

        Post savedPost = postService.createMemberPost(request, loginMemberId, loginFamilyId);
        log.info("Member {} has created post {}", loginMemberId, savedPost.getId());

        return PostResponse.from(savedPost);
    }

    @Override
    public PostResponse getPost(String postId, String loginMemberId) {
        validateFamilyMember(loginMemberId, postId);

        Post memberPostProjection = postService.getMemberPostById(postId);
        return PostResponse.from(memberPostProjection);
    }

    private void validateFamilyMember(String loginMemberId, String postId) {
        String postFamilyId = postService.getMemberPostById(postId).getFamilyId();
        String loginFamilyId = memberBridge.getFamilyIdByMemberId(loginMemberId);
        if (!postFamilyId.equals(loginFamilyId)) {
            log.warn("Unauthorized access attempt: Member {} is attempting to access post {}", loginMemberId, postId);
            throw new AuthorizationFailedException();
        }
    }
}
