package com.oing.controller;


import com.oing.domain.PaginationDTO;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.*;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.MissionPostAccessDeniedFamilyException;
import com.oing.exception.MissionPostCreateAccessDeniedMemberException;
import com.oing.restapi.PostApi;
import com.oing.service.MemberBridge;
import com.oing.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                                                            String sort, PostType type, String loginMemberId, boolean available) {
        String familyId = memberBridge.getFamilyIdByMemberId(loginMemberId);

        // TODO: 미션 게시물 접근 가능한 가족인지 검증하는 로직 필요 (프론트 요청으로 나중에 추가)
        if (type == PostType.MISSION) {
            validateMissionPostAccessFamily(available);
        }
        PaginationDTO<Post> fetchResult = postService.searchMemberPost(
                page, size, date, memberId, loginMemberId, familyId,
                sort == null || sort.equalsIgnoreCase("ASC"), type
        );

        return PaginationResponse
                .of(fetchResult, page, size)
                .map(PostResponse::from);
    }

    private void validateMissionPostAccessFamily(boolean available) {
        if (!available) {
            throw new MissionPostAccessDeniedFamilyException();
        }
    }

    @Override
    public PostResponse createPost(CreatePostRequest request, PostType type, String loginFamilyId, String loginMemberId, boolean available) {
        log.info("Member {} is trying to create post", loginMemberId);

        // TODO: 미션 게시물 업로드 가능한 사용자인지 검증하는 로직 필요 (프론트 요청으로 나중에 추가)
        if (type == PostType.MISSION) {
            validateMissionPostCreateAccessMember(available);
        }
        Post savedPost = postService.createMemberPost(request, type, loginMemberId, loginFamilyId);
        log.info("Member {} has created post {}", loginMemberId, savedPost.getId());
        return PostResponse.from(savedPost);
    }

    private void validateMissionPostCreateAccessMember(boolean available) {
        if (!available) {
            throw new MissionPostCreateAccessDeniedMemberException();
        }
    }

    @Override
    public PostResponse getPost(String postId, String loginMemberId) {
        validateFamilyMember(loginMemberId, postId);

        Post memberPostProjection = postService.getMemberPostById(postId);
        return PostResponse.from(memberPostProjection);
    }

    @Override
    public SurvivalUploadStatusResponse getSurvivalUploadStatus(String memberId, String loginMemberId, String loginFamilyId) {
        validateMemberId(loginMemberId, memberId);

        if (postService.existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(memberId, loginFamilyId, PostType.SURVIVAL, LocalDate.now())) {
            return new SurvivalUploadStatusResponse(true);
        }
        return new SurvivalUploadStatusResponse(false);
    }

    @Override
    public MissionAvailableStatusResponse getMissionAvailableStatus(String memberId, String loginMemberId, boolean valid) {
        validateMemberId(loginMemberId, memberId);
        // TODO: 추후 valid 파라미터 삭제
        // TODO: 해당 회원이 미션에 참여 가능한 회원인지 검증 로직 추가
        if (valid) {
            return new MissionAvailableStatusResponse(true);
        }
        return new MissionAvailableStatusResponse(false);
    }

    private void validateFamilyMember(String loginMemberId, String postId) {
        String postFamilyId = postService.getMemberPostById(postId).getFamilyId();
        String loginFamilyId = memberBridge.getFamilyIdByMemberId(loginMemberId);
        if (!postFamilyId.equals(loginFamilyId)) {
            log.warn("Unauthorized access attempt: Member {} is attempting to access post {}", loginMemberId, postId);
            throw new AuthorizationFailedException();
        }
    }

    private void validateMemberId(String loginMemberId, String memberId) {
        if (!loginMemberId.equals(memberId)) {
            log.warn("Unauthorized access attempt: Member {} is attempting to access post", loginMemberId);
            throw new AuthorizationFailedException();
        }
    }
}
