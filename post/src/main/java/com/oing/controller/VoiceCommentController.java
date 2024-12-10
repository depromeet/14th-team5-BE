package com.oing.controller;

import com.oing.domain.CommentType;
import com.oing.domain.Post;
import com.oing.domain.VoiceComment;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PostCommentResponseV2;
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
    public PostCommentResponseV2 createPostVoiceComment(String postId,
                                                        CreatePostCommentRequest request, String loginMemberId) {
        log.info("Member {} is trying to create voice-comment", loginMemberId);
        Post post = postService.getMemberPostById(postId);

        VoiceComment savedVoiceComment = voiceCommentService.saveVoiceComment(post, request, loginMemberId);
        log.info("Member {} has created voice-comment {}", loginMemberId, savedVoiceComment.getId());

        return new PostCommentResponseV2(
                savedVoiceComment.getId(),
                CommentType.VOICE,
                postId,
                loginMemberId,
                "앱 버전 업데이트 후에 확인할 수 있는 댓글이에요",
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
