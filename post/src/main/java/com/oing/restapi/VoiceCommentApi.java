package com.oing.restapi;

import com.oing.dto.request.CreatePostVoiceCommentRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PostCommentResponseV2;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물 음성 댓글 API", description = "게시물 음성 댓글 관련 API")
@RestController
@Valid
@RequestMapping("/v1/posts/{postId}/voice-comments")
public interface VoiceCommentApi {
    @Operation(summary = "음성 댓글 S3 Presigned Url 요청", description = "S3 Presigned Url을 요청합니다.")
    @PostMapping("/audio-file-upload-request")
    PreSignedUrlResponse requestPresignedUrl(
            @Valid
            @RequestBody
            PreSignedUrlRequest request,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );

    @Operation(summary = "게시물 음성 댓글 추가", description = "게시물에 음성 댓글을 추가합니다.")
    @PostMapping
    PostCommentResponseV2 createPostVoiceComment(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Valid
            @RequestBody
            CreatePostVoiceCommentRequest request,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );

    @Operation(summary = "게시물 음성 댓글 삭제", description = "게시물에 음성 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    DefaultResponse deletePostVoiceComment(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Parameter(description = "음성 댓글 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String commentId,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );
}
