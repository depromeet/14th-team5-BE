package com.oing.controller;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostComment;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostCommentResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.restapi.MemberPostCommentApi;
import com.oing.service.MemberBridge;
import com.oing.service.MemberPostCommentService;
import com.oing.service.MemberPostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberPostCommentController implements MemberPostCommentApi {
    private final MemberPostService memberPostService;
    private final MemberPostCommentService memberPostCommentService;
    private final MemberBridge memberBridge;

    @Transactional
    @Override
    public PostCommentResponse createPostComment(String postId, CreatePostCommentRequest request, String loginMemberId) {
        log.info("Member {} is trying to create post comment", loginMemberId);
        MemberPost memberPost = memberPostService.getMemberPostById(postId);

        MemberPostComment savedComment = memberPostCommentService.savePostComment(memberPost, request, loginMemberId);
        log.info("Member {} has created post comment {}", loginMemberId, savedComment.getId());
        return PostCommentResponse.from(savedComment);
    }

    @Transactional
    @Override
    public DefaultResponse deletePostComment(String postId, String commentId, String loginMemberId) {
        log.info("Member {} is trying to delete post comment {}", loginMemberId, commentId);
        MemberPost memberPost = memberPostService.getMemberPostById(postId);

        memberPostCommentService.deletePostComment(memberPost, commentId, loginMemberId);
        log.info("Member {} has deleted post comment {}", loginMemberId, commentId);
        return DefaultResponse.ok();
    }

    @Transactional
    @Override
    public PostCommentResponse updatePostComment(String postId, String commentId, UpdatePostCommentRequest request,
                                                 String loginMemberId) {
        log.info("Member {} is trying to update post comment {}", loginMemberId, commentId);

        MemberPostComment updatedComment = memberPostCommentService.updateMemberPostComment(postId, commentId,
                request.content(), loginMemberId);
        log.info("Member {} has updated post comment {}", loginMemberId, commentId);
        return PostCommentResponse.from(updatedComment);
    }

    @Transactional
    @Override
    public PaginationResponse<PostCommentResponse> getPostComments(String postId, Integer page, Integer size, String sort,
                                                                   String loginMemberId) {
        MemberPost memberPost = memberPostService.getMemberPostById(postId);
        if (!memberBridge.isInSameFamily(loginMemberId, memberPost.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting comment operation on post {}", loginMemberId, postId);
            throw new AuthorizationFailedException();
        }

        PaginationDTO<MemberPostComment> fetchResult = memberPostCommentService.searchPostComments(
                page, size, postId, sort == null || sort.equalsIgnoreCase("ASC")
        );

        return PaginationResponse
                .of(fetchResult, page, size)
                .map(PostCommentResponse::from);
    }
}
