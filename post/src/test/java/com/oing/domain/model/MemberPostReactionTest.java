package com.oing.domain.model;

import com.oing.domain.Emoji;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MemberPostReactionTest {

    @DisplayName("MemberPostReaction 생성자 및 getter 테스트")
    @Test
    void testMemberPostReactionConstructorAndGetters() {
        // Given
        String postId = "samplePostId";
        String memberId = "sampleMemberId";
        String imageUrl = "https://picsum.photos/200/300?random=1";
        String content = "밥 맛있다!";
        String reactionId = "sampleCommentId";
        Emoji emoji = Emoji.HEART;
        LocalDate postDate = LocalDate.of(2023, 7, 8);
        MemberPost post = new MemberPost(postId, memberId, postDate, imageUrl, content, 0, 0,
                null, null);

        // When
        MemberPostReaction reaction = new MemberPostReaction(reactionId, post, memberId, emoji);

        // Then
        assertNotNull(reaction);
        assertEquals(reactionId, reaction.getId());
    }
}
