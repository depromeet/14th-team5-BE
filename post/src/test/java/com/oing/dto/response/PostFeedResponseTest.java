package com.oing.dto.response;

import com.oing.domain.PostType;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 12/5/23
 * Time: 7:32 PM
 */
public class PostFeedResponseTest {
    @Test
    void testPostFeedResponse() {
        // Given
        String postId = "01HGW2N7EHJVJ4CJ999RRS2E97";
        String authorId = "01HGW2N7EHJVJ4CJ999RRS2E97";
        Integer commentCount = 3;
        Integer emojiCount = 2;
        String imageUrl = "https://asset.no5ing.kr/post/01HGW2N7EHJVJ4CJ999RRS2E97";
        String content = "맛있는 밥!";
        ZonedDateTime createdAt = ZonedDateTime.parse("2021-12-05T12:30:00.000+09:00");

        // When
        PostResponse postFeedResponse = new PostResponse(postId, authorId, PostType.SURVIVAL.getTypeKey(), null, commentCount, emojiCount, imageUrl,
                content, createdAt);

        // Then
        assertNotNull(postFeedResponse);
        assertEquals(postId, postFeedResponse.postId());
        assertEquals(authorId, postFeedResponse.authorId());
        assertEquals(commentCount, postFeedResponse.commentCount());
        assertEquals(emojiCount, postFeedResponse.emojiCount());
        assertEquals(imageUrl, postFeedResponse.imageUrl());
        assertEquals(createdAt, postFeedResponse.createdAt());
    }
}
