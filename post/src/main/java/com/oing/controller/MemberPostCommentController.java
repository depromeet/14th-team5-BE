package com.oing.controller;

import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostCommentResponse;
import com.oing.restapi.MemberPostCommentApi;
import com.oing.service.MemberPostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class MemberPostCommentController implements MemberPostCommentApi {
    private final MemberPostCommentService memberPostCommentService;

    @Override
    public PostCommentResponse createPostComment(String postId, CreatePostCommentRequest request) {
        return null;
    }

    @Override
    public DefaultResponse deletePostComment(String postId, String commentId) {
        return null;
    }

    @Override
    public PostCommentResponse updatePostComment(String postId, String commentId, UpdatePostCommentRequest request) {
        return null;
    }

    @Override
    public PaginationResponse<PostCommentResponse> getPostReactions(String postId, Integer page, Integer size) {
        return null;
    }
}
