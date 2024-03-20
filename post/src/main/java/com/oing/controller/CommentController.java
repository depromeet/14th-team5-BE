package com.oing.controller;

import com.oing.domain.Comment;
import com.oing.domain.Post;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostCommentResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.restapi.CommentApi;
import com.oing.service.MemberBridge;
import com.oing.service.CommentService;
import com.oing.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CommentController implements CommentApi {
    private final PostService postService;
    private final CommentService commentService;
    private final MemberBridge memberBridge;

    @Transactional
    @Override
    public PostCommentResponse createPostComment(String postId, CreatePostCommentRequest request, String loginMemberId) {
        log.info("Member {} is trying to create post comment", loginMemberId);
        Post post = postService.getMemberPostById(postId);

        Comment savedComment = commentService.savePostComment(post, request, loginMemberId);
        log.info("Member {} has created post comment {}", loginMemberId, savedComment.getId());
        return PostCommentResponse.from(savedComment);
    }

    @Transactional
    @Override
    public DefaultResponse deletePostComment(String postId, String commentId, String loginMemberId) {
        log.info("Member {} is trying to delete post comment {}", loginMemberId, commentId);
        Post post = postService.getMemberPostById(postId);

        commentService.deletePostComment(post, commentId, loginMemberId);
        log.info("Member {} has deleted post comment {}", loginMemberId, commentId);
        return DefaultResponse.ok();
    }

    @Transactional
    @Override
    public PostCommentResponse updatePostComment(String postId, String commentId, UpdatePostCommentRequest request,
                                                 String loginMemberId) {
        log.info("Member {} is trying to update post comment {}", loginMemberId, commentId);

        Comment updatedComment = commentService.updateMemberPostComment(postId, commentId,
                request.content(), loginMemberId);
        log.info("Member {} has updated post comment {}", loginMemberId, commentId);
        return PostCommentResponse.from(updatedComment);
    }

    @Transactional
    @Override
    public PaginationResponse<PostCommentResponse> getPostComments(String postId, Integer page, Integer size, String sort,
                                                                   String loginMemberId) {
        Post post = postService.getMemberPostById(postId);
        if (!memberBridge.isInSameFamily(loginMemberId, post.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting comment operation on post {}", loginMemberId, postId);
            throw new AuthorizationFailedException();
        }

        PaginationDTO<Comment> fetchResult = commentService.searchPostComments(
                page, size, postId, sort == null || sort.equalsIgnoreCase("ASC")
        );

        return PaginationResponse
                .of(fetchResult, page, size)
                .map(PostCommentResponse::from);
    }
}
