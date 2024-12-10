package com.oing.restapi;

import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostCommentResponse;
import com.oing.dto.response.PostCommentResponseV2;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물 댓글 API", description = "게시물 댓글 관련 API")
@RestController
@Valid
@RequestMapping("/v1/posts/{postId}/comments")
public interface CommentApi {
    @Operation(summary = "게시물 댓글 추가", description = "게시물에 댓글을 추가합니다.")
    @PostMapping
    PostCommentResponse createPostComment(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Valid
            @RequestBody
            CreatePostCommentRequest request,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );

    @Operation(summary = "게시물 댓글 삭제", description = "게시물에 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    DefaultResponse deletePostComment(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Parameter(description = "댓글 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String commentId,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );

    @Operation(summary = "게시물 댓글 수정", description = "게시물에 댓글을 수정합니다.")
    @PutMapping("/{commentId}")
    PostCommentResponse updatePostComment(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Parameter(description = "댓글 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String commentId,

            @Valid
            @RequestBody
            UpdatePostCommentRequest request,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );


    @Operation(summary = "게시물 댓글 조회", description = "게시물에 달린 댓글을 조회합니다.")
    @GetMapping
    PaginationResponse<PostCommentResponseV2> getPostComments(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "가져올 현재 페이지", example = "1")
            @Min(value = 1)
            Integer page,

            @RequestParam(required = false, defaultValue = "10")
            @Parameter(description = "가져올 페이지당 크기", example = "10")
            @Min(value = 1)
            Integer size,

            @RequestParam(required = false)
            @Parameter(description = "정렬 방식", example = "DESC | ASC")
            String sort,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );
}
