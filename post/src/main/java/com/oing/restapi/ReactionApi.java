package com.oing.restapi;

import com.oing.dto.request.PostReactionRequest;
import com.oing.dto.response.*;
import com.oing.util.security.LoginMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물 반응 API", description = "게시물 반응 관련 API")
@RestController
@Valid
@RequestMapping("/v1/posts/{postId}/reactions")
public interface ReactionApi {
    @Operation(summary = "게시물 반응 추가", description = "게시물에 반응합니다.")
    @PostMapping
    DefaultResponse createPostReaction(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId,

            @Valid
            @RequestBody
            PostReactionRequest request
    );

    @Operation(summary = "게시물 반응 삭제", description = "게시물에 반응을 삭제합니다.")
    @DeleteMapping
    DefaultResponse deletePostReaction(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId,

            @Valid
            @RequestBody
            PostReactionRequest request
    );

    @Operation(summary = "게시물 반응 요약 조회", description = "게시묿 반응 요약을 조회합니다.")
    @GetMapping("/summary")
    PostReactionSummaryResponse getPostReactionSummary(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );

    @Operation(summary = "게시물 반응 전체 조회", description = "게시물에 달린 모든 반응 목록을 조회합니다.")
    @GetMapping
    ArrayResponse<PostReactionResponse> getPostReactions(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );

    @Operation(summary = "게시물 반응을 남긴 전체 멤버 조회", description = "게시물에 반응을 남긴 모든 멤버 목록을 조회합니다.")
    @GetMapping("/member")
    PostReactionMemberResponse getPostReactionMembers(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Parameter(hidden = true)
            @LoginMemberId
            String loginMemberId
    );
}
