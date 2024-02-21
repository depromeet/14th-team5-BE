package com.oing.restapi;

import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.dto.response.*;
import com.oing.util.security.LoginFamilyId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물 리얼 이모지 API", description = "게시물 리얼 이모지 관련 API")
@RestController
@Valid
@RequestMapping("/v1/posts/{postId}/real-emoji")
public interface MemberPostRealEmojiApi {

    @Operation(summary = "게시물에 리얼 이모지 등록", description = "게시물에 리얼 이모지를 추가합니다.")
    @PostMapping
    PostRealEmojiResponse createPostRealEmoji(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Parameter(hidden = true)
            @LoginFamilyId
            String familyId,

            @Valid
            @RequestBody
            PostRealEmojiRequest request
    );

    @Operation(summary = "게시물에서 리얼 이모지 삭제", description = "게시물에서 리얼 이모지를 삭제합니다.")
    @DeleteMapping("/{realEmojiId}")
    DefaultResponse deletePostRealEmoji(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Parameter(description = "리얼 이모지 ID", example = "01HEFDFADFDFDAFDFDARS2E97")
            @PathVariable
            String realEmojiId
    );

    @Operation(summary = "게시물의 리얼 이모지 요약 조회", description = "게시물에 달린 리얼 이모지 요약을 조회합니다.")
    @GetMapping("/summary")
    PostRealEmojiSummaryResponse getPostRealEmojiSummary(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId
    );

    @Operation(summary = "게시물의 리얼 이모지 전체 조회", description = "게시물에 달린 모든 리얼 이모지 목록을 조회합니다.")
    @GetMapping
    ArrayResponse<PostRealEmojiResponse> getPostRealEmojis(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId
    );

    @Operation(summary = "게시물의 리얼 이모지를 남긴 전체 멤버 조회", description = "게시물에 리얼 이모지를 남긴 모든 멤버 목록을 조회합니다.")
    @GetMapping("/member")
    PostRealEmojiMemberResponse getPostRealEmojiMembers(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId
    );
}
