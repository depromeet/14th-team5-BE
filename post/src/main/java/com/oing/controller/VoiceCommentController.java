package com.oing.controller;

import com.oing.domain.CommentType;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostCommentResponseV2;
import com.oing.restapi.VoiceCommentApi;
import org.springframework.stereotype.Controller;

import java.time.ZonedDateTime;
import java.util.List;

@Controller
public class VoiceCommentController implements VoiceCommentApi {
    @Override
    public PostCommentResponseV2 createPostVoiceComment(String postId,
                                                        CreatePostCommentRequest request, String loginMemberId) {
        return new PostCommentResponseV2(
                "01HGW2N7EHJVJ4CJ999RRS2E97",
                CommentType.VOICE,
                postId,
                loginMemberId,
                "앱 버전 업데이트 후에 확인할 수 있는 댓글이에요",
                "https://..",
                ZonedDateTime.now()
        );
    }

    @Override
    public DefaultResponse deletePostVoiceComment(String postId, String commentId, String loginMemberId) {
        return DefaultResponse.ok();
    }

    @Override
    public PaginationResponse<PostCommentResponseV2> getPostComments(String postId, Integer page, Integer size,
                                                                     String sort, String loginMemberId) {
        return new PaginationResponse<>(
                1,
                1,
                size,
                false,
                List.of(
                        new PostCommentResponseV2(
                                "01HGW2N7EHJVJ4CJ999RRS2E97",
                                CommentType.VOICE,
                                postId,
                                loginMemberId,
                                "앱 버전 업데이트 후에 확인할 수 있는 댓글이에요",
                                "https://..",
                                ZonedDateTime.now()
                        )
                )
        );
    }
}
