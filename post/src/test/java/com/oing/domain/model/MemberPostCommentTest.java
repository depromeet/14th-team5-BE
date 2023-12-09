package com.oing.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MemberPostCommentTest {

    @DisplayName("MemberPostComment 생성자 및 getter 테스트")
    @Test
    void testMemberPostCommentConstructorAndGetters() {
        // Given
        String postId = "samplePostId";
        String memberId = "sampleMemberId";
        String commentId = "sampleCommentId";
        String commentContents = "sampleCommentContents";
        LocalDate postDate = LocalDate.of(2023, 7, 8);
        MemberPost post = new MemberPost(postId, memberId, postDate, null, 0, 0,
                null, null);

        // When
        MemberPostComment comment = new MemberPostComment(commentId, post, memberId, commentContents);

        // Then
        assertNotNull(comment);
        assertEquals(commentId, comment.getId());
    }
}
