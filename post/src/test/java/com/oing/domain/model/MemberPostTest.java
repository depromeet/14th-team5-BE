package com.oing.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MemberPostTest {

    @DisplayName("MemberPost 생성자 및 getter 테스트")
    @Test
    void testMemberPostConstructorAndGetters() {
        // Given
        String postId = "samplePostId";
        String memberId = "sampleMemberId";
        LocalDate postDate = LocalDate.of(2023, 7, 8);

        // When
        MemberPost post = new MemberPost(postId, memberId, postDate, null, 0, 0,
                null, null);

        // Then
        assertNotNull(post);
        assertEquals(postId, post.getId());
    }
}
