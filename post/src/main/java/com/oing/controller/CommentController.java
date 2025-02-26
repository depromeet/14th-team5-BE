package com.oing.controller;

import com.oing.domain.*;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostCommentResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.restapi.CommentApi;
import com.oing.service.CommentService;
import com.oing.service.MemberBridge;
import com.oing.service.PostService;
import com.oing.service.VoiceCommentService;
import com.oing.util.Paginator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CommentController implements CommentApi {
    private final PostService postService;
    private final CommentService commentService;
    private final VoiceCommentService voiceCommentService;
    private final MemberBridge memberBridge;

    @Override
    @Transactional
    public PostCommentResponse createPostComment(String postId, CreatePostCommentRequest request, String loginMemberId) {
        log.info("Member {} is trying to create post comment", loginMemberId);
        Post post = postService.getMemberPostById(postId);

        Comment savedComment = commentService.savePostComment(post, request, loginMemberId);
        log.info("Member {} has created post comment {}", loginMemberId, savedComment.getId());
        return new PostCommentResponse(
                savedComment.getId(),
                CommentType.TEXT,
                postId,
                savedComment.getMemberId(),
                savedComment.getContent(),
                null,
                savedComment.getCreatedAt().atZone(ZoneId.systemDefault())
        );
    }

    @Override
    @Transactional
    public DefaultResponse deletePostComment(String postId, String commentId, String loginMemberId) {
        log.info("Member {} is trying to delete post comment {}", loginMemberId, commentId);
        Post post = postService.getMemberPostById(postId);

        commentService.deletePostComment(post, commentId, loginMemberId);
        log.info("Member {} has deleted post comment {}", loginMemberId, commentId);
        return DefaultResponse.ok();
    }

    @Override
    @Transactional
    public PostCommentResponse updatePostComment(String postId, String commentId, UpdatePostCommentRequest request,
                                                 String loginMemberId) {
        log.info("Member {} is trying to update post comment {}", loginMemberId, commentId);

        Comment updatedComment = commentService.updateMemberPostComment(postId, commentId,
                request.content(), loginMemberId);
        log.info("Member {} has updated post comment {}", loginMemberId, commentId);
        return new PostCommentResponse(
                updatedComment.getId(),
                CommentType.TEXT,
                postId,
                updatedComment.getMemberId(),
                updatedComment.getContent(),
                null,
                updatedComment.getCreatedAt().atZone(ZoneId.systemDefault())
        );
    }

    @Override
    public PaginationResponse<PostCommentResponse> getPostComments(String postId, Integer page, Integer size, String sort,
                                                                     String loginMemberId) {
        Post post = postService.getMemberPostById(postId);
        validateAuthorization(loginMemberId, post);

        List<BaseComment> comments = new ArrayList<>();
        comments.addAll(commentService.getPostComments(postId));
        comments.addAll(voiceCommentService.getPostVoiceComments(postId));
        comments.sort(BaseComment.getComparator(sort));

        // 페이징 처리
        List<PostCommentResponse> response = comments.stream()
                .map(comment -> mapToPostCommentResponse(comment, postId))
                .toList();
        return paginateComments(response, page, size);
    }

    private void validateAuthorization(String loginMemberId, Post post) {
        if (!memberBridge.isInSameFamily(loginMemberId, post.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting comment operation on post {}", loginMemberId, post.getId());
            throw new AuthorizationFailedException();
        }
    }

    private PostCommentResponse mapToPostCommentResponse(BaseComment baseComment, String postId) {
        return new PostCommentResponse(
                baseComment.getId(),
                baseComment instanceof Comment ? CommentType.TEXT : CommentType.VOICE,
                postId,
                baseComment.getMemberId(),
                baseComment instanceof Comment ? baseComment.getContent() : null,
                baseComment instanceof VoiceComment ? baseComment.getContent() : null,
                baseComment.getCreatedAt().atZone(ZoneId.systemDefault())
        );
    }

    private PaginationResponse<PostCommentResponse> paginateComments(List<PostCommentResponse> comments, Integer page, Integer size) {
        Paginator<PostCommentResponse> paginator = new Paginator<>(comments, size);
        PaginationDTO<PostCommentResponse> paginationDTO = paginator.getPage(page);

        return PaginationResponse.of(paginationDTO, page, size);
    }
}
