package com.oing.controller;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostComment;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostCommentResponse;
import com.oing.restapi.MemberPostCommentApi;
import com.oing.service.MemberPostCommentService;
import com.oing.service.MemberPostService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class MemberPostCommentController implements MemberPostCommentApi {
    private final AuthenticationHolder authenticationHolder;
    private final IdentityGenerator identityGenerator;
    private final MemberPostService memberPostService;
    private final MemberPostCommentService memberPostCommentService;

    @Override
    public PostCommentResponse createPostComment(String postId, CreatePostCommentRequest request) {
        String memberId = authenticationHolder.getUserId();
        MemberPost memberPost = memberPostService.getMemberPostById(postId);
        MemberPostComment memberPostComment = new MemberPostComment(
                identityGenerator.generateIdentity(),
                memberPost,
                memberId,
                request.content()
        );
        MemberPostComment savedMemberPostComment = memberPostCommentService
                .savePostComment(memberPostComment);
        return PostCommentResponse.from(savedMemberPostComment);
    }

    @Override
    public DefaultResponse deletePostComment(String postId, String commentId) {
        memberPostCommentService.deletePostComment(postId, commentId);
        return DefaultResponse.ok();
    }

    @Override
    public PostCommentResponse updatePostComment(String postId, String commentId, UpdatePostCommentRequest request) {
        MemberPostComment memberPostComment = memberPostCommentService.getMemberPostComment(postId, commentId);
        memberPostComment.setContent(request.content());
        MemberPostComment savedMemberPostComment = memberPostCommentService
                .savePostComment(memberPostComment);
        return PostCommentResponse.from(savedMemberPostComment);
    }

    @Override
    public PaginationResponse<PostCommentResponse> getPostComments(String postId, Integer page, Integer size, String sort) {
        PaginationDTO<MemberPostComment> fetchResult = memberPostCommentService.searchPostComments(
                page, size, postId, sort == null || sort.equalsIgnoreCase("ASC")
        );

        return PaginationResponse
                .of(fetchResult, page, size)
                .map(PostCommentResponse::from);
    }
}
