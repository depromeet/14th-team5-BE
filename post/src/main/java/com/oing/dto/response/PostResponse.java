package com.oing.dto.response;

import com.oing.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:30 PM
 */
@Schema(description = "피드 게시물 응답")
public record PostResponse(
        @Schema(description = "피드 게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String postId,

        @Schema(description = "피드 게시물 작성자 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String authorId,

        @Schema(description = "게시물 타입", example = "MISSION")
        String type,

        @Schema(description = "미션 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String missionId,

        @Schema(description = "피드 게시물 댓글 수", example = "3")
        Integer commentCount,

        @Schema(description = "피드 게시물 반응 수", example = "2")
        Integer emojiCount,

        @Schema(description = "피드 게시물 사진 주소", example = "https://asset.no5ing.kr/post/01HGW2N7EHJVJ4CJ999RRS2E97")
        String imageUrl,

        @Schema(description = "피드 게시물 내용", example = "맛있는 밥!")
        String content,

        @Schema(description = "피드 작성 시간", example = "2023-12-23T01:53:21.577347+09:00")
        ZonedDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getMemberId(),
                post.getType().getTypeKey(),
                post.getMissionId(),
                post.getCommentCnt(),
                post.getReactionCnt() + post.getRealEmojiCnt(),
                post.getPostImgUrl(),
                post.getContent(),
                post.getCreatedAt().atZone(ZoneId.systemDefault())
        );
    }
}
