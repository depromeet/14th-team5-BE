package com.oing.domain.model;

import com.oing.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostTest {

    @DisplayName("MemberPost 생성자 및 getter 테스트")
    @Test
    void testMemberPostConstructorAndGetters() {
        // Given
        String postId = "samplePostId";
        String memberId = "sampleMemberId";
        String familyId = "sampleFamilyId";
        String imageUrl = "https://picsum.photos/200/300?random=1";
        String imageKey = "/200/300?random=1";
        String content = "밥 맛있다!";

        // When
        Post post = new Post(postId, memberId, familyId, imageUrl, imageKey, content, 0,
                0, 0, null, null, null);

        // Then
        assertNotNull(post);
        assertEquals(postId, post.getId());
    }
}
