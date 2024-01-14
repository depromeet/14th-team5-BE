package com.oing.restapi;

import com.oing.dto.request.CreateMyRealEmojiRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.MyPostRealEmojisResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물 리얼 이모지 API", description = "게시물 리얼 이모지 관련 API")
@RestController
@Valid
@RequestMapping("/v1/real-emoji")
public interface MemberRealEmojiApi {

    @Operation(summary = "리얼 이모지 사진 Presigned Url 요청", description = "S3 Presigned Url을 요청합니다.")
    @PostMapping("/image-upload-request")
    PreSignedUrlResponse requestPresignedUrl(
            @Valid
            @RequestBody
            PreSignedUrlRequest request
    );

    @Operation(summary = "자신의 리얼 이모지 추가", description = "자신의 리얼 이모지를 추가합니다.")
    @PostMapping
    DefaultResponse createMyRealEmoji(
            @Valid
            @RequestBody
            CreateMyRealEmojiRequest request
    );

    @Operation(summary = "자신의 리얼 이모지 변경", description = "자신의 리얼 이모지 사진을 변경합니다.")
    @PutMapping("/{realEmojiId}")
    DefaultResponse changeMyRealEmoji(
            @Parameter(description = "리얼 이모지 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String realEmojiId,

            @Valid
            @RequestBody
            CreateMyRealEmojiRequest request
    );

    @Operation(summary = "회원의 리얼 이모지 조회", description = "자신의 리얼 이모지를 조회합니다.")
    @GetMapping("/{memberId}")
    MyPostRealEmojisResponse getMyRealEmojis(
            @Parameter(description = "회원 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String memberId
    );
}
