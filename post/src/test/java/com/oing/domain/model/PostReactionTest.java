package com.oing.domain.model;

import com.oing.domain.Emoji;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.oing.domain.Reaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostReactionTest {

    @DisplayName("MemberPostReaction 생성자 및 getter 테스트")
    @Test
    void testMemberPostReactionConstructorAndGetters() {
        // Given
        String postId = "samplePostId";
        String memberId = "sampleMemberId";
        String familyId = "sampleFamilyId";
        String imageUrl = "https://picsum.photos/200/300?random=1";
        String imageKey = "/200/300?random=1";
        String content = "밥 맛있다!";
        String reactionId = "sampleCommentId";
        Emoji emoji = Emoji.EMOJI_1;
        Post post = new Post(postId, memberId, familyId, null, PostType.SURVIVAL, imageUrl, imageKey, content, 0,
                0, 0, null, null, null);

        // When
        Reaction reaction = new Reaction(reactionId, post, memberId, emoji);

        // Then
        assertNotNull(reaction);
        assertEquals(reactionId, reaction.getId());
    }
}
