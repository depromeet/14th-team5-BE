package com.oing.restapi;

import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.dto.response.ArrayResponse;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PostRealEmojiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물 리얼 이모지 API", description = "게시물 리얼 이모지 관련 API")
@RestController
@Valid
@RequestMapping("/v1/posts/real-emoji")
public interface MemberPostRealEmojiApi {

    @Operation(summary = "게시물에 리얼 이모지 등록", description = "게시물에 리얼 이모지를 추가합니다.")
    @PostMapping("/{postId}")
    DefaultResponse createRealEmoji(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Valid
            @RequestBody
            PostRealEmojiRequest request
    );

    @Operation(summary = "게시물에서 리얼 이모지 삭제", description = "게시물에서 리얼 이모지를 삭제합니다.")
    @DeleteMapping("/{postId}")
    DefaultResponse deleteRealEmoji(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @Valid
            @RequestBody
            PostRealEmojiRequest request
    );

    @Operation(summary = "게시물의 리얼 이모지 전체 조회", description = "게시물에 달린 모든 리얼 이모지 목록을 조회합니다.")
    @GetMapping("/{postId}")
    ArrayResponse<PostRealEmojiResponse> getPostRealEmojis(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId
    );
}
