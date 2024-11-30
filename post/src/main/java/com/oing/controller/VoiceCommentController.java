package com.oing.controller;

import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostVoiceCommentResponse;
import com.oing.restapi.VoiceCommentApi;
import org.springframework.stereotype.Controller;

import java.time.ZonedDateTime;
import java.util.List;

@Controller
public class VoiceCommentController implements VoiceCommentApi {
    @Override
    public PostVoiceCommentResponse createPostVoiceComment(String postId,
                                                           CreatePostCommentRequest request, String loginMemberId) {
        return new PostVoiceCommentResponse(
                "01HGW2N7EHJVJ4CJ999RRS2E97",
                postId,
                loginMemberId,
                "https://..",
                ZonedDateTime.now()
        );
    }

    @Override
    public DefaultResponse deletePostVoiceComment(String postId, String commentId, String loginMemberId) {
        return DefaultResponse.ok();
    }

    @Override
    public PaginationResponse<PostVoiceCommentResponse> getPostComments(String postId, Integer page, Integer size,
                                                                        String sort, String loginMemberId) {
        return new PaginationResponse<>(
                1,
                1,
                size,
                false,
                List.of(
                        new PostVoiceCommentResponse(
                                "01HGW2N7EHJVJ4CJ999RRS2E97",
                                postId,
                                loginMemberId,
                                "https://..",
                                ZonedDateTime.now()
                        )
                )
        );
    }
}
