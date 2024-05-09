package com.oing.domain.model;

import com.oing.domain.Comment;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostCommentTest {

    @DisplayName("MemberPostComment 생성자 및 getter 테스트")
    @Test
    void testMemberPostCommentConstructorAndGetters() {
        // Given
        String postId = "samplePostId";
        String memberId = "sampleMemberId";
        String familyId = "sampleFamilyId";
        String imageUrl = "https://picsum.photos/200/300?random=1";
        String imageKey = "/200/300?random=1";
        String content = "밥 맛있다!";
        String commentId = "sampleCommentId";
        String commentContents = "sampleCommentContents";
        Post post = new Post(postId, memberId, familyId, null, PostType.SURVIVAL, imageUrl, imageKey, content, 0,
                0, 0, null, null, null);

        // When
        Comment comment = new Comment(commentId, post, memberId, commentContents);

        // Then
        assertNotNull(comment);
        assertEquals(commentId, comment.getId());
    }
}
