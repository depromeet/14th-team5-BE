package com.oing.controller;

import com.oing.domain.*;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostCommentResponse;
import com.oing.dto.response.PostCommentResponseV2;
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
        return PostCommentResponse.from(savedComment);
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
        return PostCommentResponse.from(updatedComment);
    }

    @Override
    public PaginationResponse<PostCommentResponseV2> getPostComments(String postId, Integer page, Integer size, String sort,
                                                                     String loginMemberId) {
        Post post = postService.getMemberPostById(postId);
        validateAuthorization(loginMemberId, post);

        List<BaseComment> comments = new ArrayList<>();
        comments.addAll(commentService.getPostComments(postId));
        comments.addAll(voiceCommentService.getPostVoiceComments(postId));
        comments.sort(BaseComment.getComparator(sort));

        // 페이징 처리
        List<PostCommentResponseV2> response = comments.stream()
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

    private PostCommentResponseV2 mapToPostCommentResponse(BaseComment baseComment, String postId) {
        String content = baseComment instanceof Comment ? baseComment.getContent() : null;
        String audioUrl = baseComment instanceof VoiceComment ? baseComment.getContent() : null;

        return new PostCommentResponseV2(
                baseComment.getId(),
                baseComment instanceof Comment ? CommentType.TEXT : CommentType.VOICE,
                postId,
                baseComment.getMemberId(),
                content,
                audioUrl,
                baseComment.getCreatedAt().atZone(ZoneId.systemDefault())
        );
    }

    private PaginationResponse<PostCommentResponseV2> paginateComments(List<PostCommentResponseV2> comments, Integer page, Integer size) {
        Paginator<PostCommentResponseV2> paginator = new Paginator<>(comments, size);
        PaginationDTO<PostCommentResponseV2> paginationDTO = paginator.getPage(page);

        return PaginationResponse.of(paginationDTO, page, size);
    }
}
