package com.oing.controller;


import com.oing.domain.PaginationDTO;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.oing.dto.dto.PostRankerDTO;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.*;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.MissionPostAccessDeniedFamilyException;
import com.oing.exception.MissionPostCreateAccessDeniedMemberException;
import com.oing.restapi.PostApi;
import com.oing.service.CommentService;
import com.oing.service.MemberBridge;
import com.oing.service.PostService;
import com.oing.service.ReactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

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
    private final CommentService commentService;
    private final ReactionService reactionService;
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
    public PostResponse findLatestPost(LocalDate inclusiveStartDate, LocalDate exclusiveEndDate, PostType postType, String loginFamilyId) {
        Post latestPost = postService.findLatestPost(inclusiveStartDate, exclusiveEndDate, postType, loginFamilyId);
        if (latestPost == null) return null;

        return PostResponse.from(latestPost);
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
    public MissionUploadStatusResponse getMissionUploadStatus(String memberId, String loginMemberId, String loginFamilyId) {
        validateMemberId(loginMemberId, memberId);

        if (postService.existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(memberId, loginFamilyId, PostType.MISSION, LocalDate.now())) {
            return new MissionUploadStatusResponse(true);
        }
        return new MissionUploadStatusResponse(false);
    }

    @Override
    public MissionAvailableStatusResponse getMissionAvailableStatus(String memberId, String loginMemberId, String loginFamilyId) {
        validateMemberId(loginMemberId, memberId);

        if (postService.isCreatedSurvivalPostByMajority(loginFamilyId)) {
            return new MissionAvailableStatusResponse(true);
        }
        return new MissionAvailableStatusResponse(false);
    }

    @Override
    public RemainingSurvivalPostCountResponse getRemainingSurvivalPostCount(String memberId, String loginMemberId, String loginFamilyId) {
        validateMemberId(loginMemberId, memberId);

        int remainingSurvivalPostCount = postService.calculateRemainingSurvivalPostCountUntilMissionUnlocked(loginFamilyId);
        return new RemainingSurvivalPostCountResponse(remainingSurvivalPostCount);
    }

    @Override
    public ArrayResponse<PostRankerResponse> getFamilyMembersMonthlySurvivalRanking(String loginFamilyId) {
        List<String> familyMembersIds = memberBridge.getFamilyMembersIdsByFamilyId(loginFamilyId);
        LocalDate dateTime = ZonedDateTime.now().toLocalDate();

        List<PostRankerResponse> postRankerResponses = familyMembersIds.stream().map(familyMemberId -> new PostRankerDTO(
                        familyMemberId,
                        postService.countMonthlyPostByMemberId(dateTime, familyMemberId),
                        commentService.countMonthlyCommentByMemberId(dateTime, familyMemberId),
                        reactionService.countMonthlyReactionByMemberId(dateTime, familyMemberId)

                ))
                .filter(postRankerDTO -> postRankerDTO.getPostCount() > 0) // 게시글이 없는 경우 제외
                .sorted() // 내부 정책에 따라 재정의한 DTO compareTo 메서드를 통해 정렬
                .map(postRankerDTO -> new PostRankerResponse(
                        postRankerDTO.getMemberId(),
                        postRankerDTO.getPostCount().intValue())
                )
                .toList();

        return ArrayResponse.of(postRankerResponses);
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
