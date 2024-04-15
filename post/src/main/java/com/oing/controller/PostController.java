package com.oing.controller;


import com.oing.domain.PaginationDTO;
import com.oing.domain.Post;
import com.oing.domain.Type;
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
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
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
public class PostController implements PostApi {

    private final PostService postService;
    private final MemberBridge memberBridge;

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request, String loginMemberId) {
        return postService.requestPresignedUrl(loginMemberId, request.imageName());
    }

    @Override
    public PaginationResponse<PostResponse> fetchDailyFeeds(Integer page, Integer size, LocalDate date, String memberId,
                                                            String sort, Type type, String loginMemberId) {
        String familyId = memberBridge.getFamilyIdByMemberId(loginMemberId);
        // TODO: type이 mission이라면 사용자 검증 로직 추가
        PaginationDTO<Post> fetchResult = postService.searchMemberPost(
                page, size, date, memberId, loginMemberId, familyId,
                sort == null || sort.equalsIgnoreCase("ASC"), type
        );

        return PaginationResponse
                .of(fetchResult, page, size)
                .map(PostResponse::from);
    }

    @Override
    public PostResponse createPost(CreatePostRequest request, Type type, String loginFamilyId, String loginMemberId) {
        if (type.equals(Type.SURVIVAL)) {
            log.info("Member {} is trying to create post", loginMemberId);

            Post savedPost = postService.createMemberPost(request, type, loginMemberId, loginFamilyId);
            log.info("Member {} has created post {}", loginMemberId, savedPost.getId());
            return PostResponse.from(savedPost);
        } else {
            // 미션 API 응답 모킹을 위해 if-else 문으로 분기 처리했습니다 (추후 삭제 예정)
            return new PostResponse("01HGW2N7EHJVJ4CJ999RRS2E97", "01HGWOODDDFFF4CJ999RRS2E111",
                    "MISSION", "01HGW2N7EHJVJ4CJ999RRS2E97", 3, 2, "https://asset.no5ing.kr/post/01HGW2N7EHJVJ4CJ999RRS2E97", "맛있는 밥!", ZonedDateTime.now());
        }

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
