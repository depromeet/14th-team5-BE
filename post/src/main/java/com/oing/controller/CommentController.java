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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
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
    @Transactional
    public PaginationResponse<PostCommentResponseV2> getPostComments(String postId, Integer page, Integer size, String sort,
                                                                     String loginMemberId) {
        Post post = postService.getMemberPostById(postId);
        validateAuthorization(loginMemberId, post);

        List<Comment> comments = commentService.getPostComments(postId);
        List<VoiceComment> voiceComments = voiceCommentService.getPostVoiceComments(postId);

        // 댓글과 음성 댓글 통합 및 정렬
        List<PostCommentResponseV2> combinedComments = combineComments(comments, voiceComments);
        combinedComments.sort(getCommentComparator(sort));

        // 페이징 처리
        return paginateComments(combinedComments, page, size);
    }

    private void validateAuthorization(String loginMemberId, Post post) {
        if (!memberBridge.isInSameFamily(loginMemberId, post.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting comment operation on post {}", loginMemberId, post.getId());
            throw new AuthorizationFailedException();
        }
    }

    private List<PostCommentResponseV2> combineComments(List<Comment> comments, List<VoiceComment> voiceComments) {
        List<PostCommentResponseV2> textComments = comments.stream()
                .map(this::mapToTextComment)
                .toList();

        List<PostCommentResponseV2> voiceCommentResponses = voiceComments.stream()
                .map(this::mapToVoiceComment)
                .toList();

        List<PostCommentResponseV2> combinedComments = new ArrayList<>();
        combinedComments.addAll(textComments);
        combinedComments.addAll(voiceCommentResponses);

        return combinedComments;
    }

    private PostCommentResponseV2 mapToTextComment(Comment comment) {
        return new PostCommentResponseV2(
                comment.getId(),
                CommentType.TEXT,
                comment.getPost().getId(),
                comment.getMemberId(),
                comment.getContent(),
                null,
                comment.getCreatedAt().atZone(ZoneId.systemDefault())
        );
    }

    private PostCommentResponseV2 mapToVoiceComment(VoiceComment voiceComment) {
        return new PostCommentResponseV2(
                voiceComment.getId(),
                CommentType.VOICE,
                voiceComment.getPost().getId(),
                voiceComment.getMemberId(),
                null,
                voiceComment.getAudioUrl(),
                voiceComment.getCreatedAt().atZone(ZoneId.systemDefault())
        );
    }

    private Comparator<? super PostCommentResponseV2> getCommentComparator(String sort) {
        return (c1, c2) -> {
            if (sort == null || sort.equalsIgnoreCase("ASC")) {
                return c1.commentId().compareTo(c2.commentId());
            } else {
                return c2.commentId().compareTo(c1.commentId());
            }
        };
    }

    private PaginationResponse<PostCommentResponseV2> paginateComments(List<PostCommentResponseV2> comments, Integer page, Integer size) {
        int total = comments.size();
        int totalPage = (int) Math.ceil((double) total / size);
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);

        List<PostCommentResponseV2> paginatedComments = comments.subList(start, end);
        PaginationDTO<PostCommentResponseV2> paginationDTO = new PaginationDTO<>(totalPage, paginatedComments);

        return PaginationResponse.of(paginationDTO, page, size);
    }
}
