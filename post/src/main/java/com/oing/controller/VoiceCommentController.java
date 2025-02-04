package com.oing.controller;

import com.oing.domain.CommentType;
import com.oing.domain.Post;
import com.oing.domain.VoiceComment;
import com.oing.dto.request.CreatePostVoiceCommentRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PostCommentResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.restapi.VoiceCommentApi;
import com.oing.service.PostService;
import com.oing.service.VoiceCommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.time.ZonedDateTime;

@Slf4j
@RequiredArgsConstructor
@Controller
public class VoiceCommentController implements VoiceCommentApi {

    private final VoiceCommentService voiceCommentService;
    private final PostService postService;

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request, String loginMemberId) {
        return voiceCommentService.requestPresignedUrl(loginMemberId, request.imageName());
    }

    @Override
    @Transactional
    public PostCommentResponse createPostVoiceComment(String postId,
                                                        CreatePostVoiceCommentRequest request, String loginMemberId) {
        log.info("Member {} is trying to create voice-comment", loginMemberId);
        Post post = postService.getMemberPostById(postId);

        VoiceComment savedVoiceComment = voiceCommentService.saveVoiceComment(post, request, loginMemberId);
        log.info("Member {} has created voice-comment {}", loginMemberId, savedVoiceComment.getId());

        return new PostCommentResponse(
                savedVoiceComment.getId(),
                CommentType.VOICE,
                postId,
                loginMemberId,
                savedVoiceComment.getAudioUrl(),
                ZonedDateTime.now()
        );
    }

    @Override
    @Transactional
    public DefaultResponse deletePostVoiceComment(String postId, String commentId, String loginMemberId) {
        log.info("Member {} is trying to delete voice-comment {}", loginMemberId, commentId);
        Post post = postService.getMemberPostById(postId);

        voiceCommentService.deleteVoiceComment(post, commentId, loginMemberId);
        log.info("Member {} has deleted voice-comment {}", loginMemberId, commentId);
        return DefaultResponse.ok();
    }
}
