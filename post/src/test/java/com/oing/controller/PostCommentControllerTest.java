package com.oing.controller;

import com.google.common.collect.Lists;
import com.oing.domain.Comment;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.oing.domain.VoiceComment;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostCommentResponse;
import com.oing.service.CommentService;
import com.oing.service.MemberBridge;
import com.oing.service.PostService;
import com.oing.service.VoiceCommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostCommentControllerTest {
    @InjectMocks
    private CommentController commentController;

    @Mock
    private PostService postService;
    @Mock
    private CommentService commentService;
    @Mock
    private VoiceCommentService voiceCommentService;
    @Mock
    private MemberBridge memberBridge;

    @Test
    void 게시물_댓글_삭제_테스트() {
        //given
        Post post = spy(new Post("1", "1", "1", PostType.SURVIVAL, "1", "1", "1"));
        Comment comment = spy(new Comment("1", post, "1", "1"));
        when(postService.getMemberPostById(post.getId())).thenReturn(post);

        //when
        commentController.deletePostComment(
                post.getId(),
                comment.getId(),
                "1"
        );

        //then
        //nothing. just check no exception
    }

    @Test
    void 게시물_댓글_목록_조회_테스트() {
        //given
        Post post = new Post(
                "1",
                "1",
                "1",
                PostType.SURVIVAL,
                "1",
                "1",
                "1"
        );
        Comment comment = spy(new Comment(
                "1",
                post,
                "1",
                "1"
        ));
        VoiceComment voiceComment = spy(new VoiceComment(
                "1",
                post,
                "1",
                "1"
        ));
        int page = 1;
        int size = 10;
        String postId = "1";
        boolean asc = true;
        List<Comment> comments = Lists.newArrayList(comment);
        List<VoiceComment> voiceComments = Lists.newArrayList(voiceComment);
        when(postService.getMemberPostById("1")).thenReturn(post);
        when(memberBridge.isInSameFamily("1", "1")).thenReturn(true);
        when(comment.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(voiceComment.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(commentService.getPostComments(postId)).thenReturn(comments);
        when(voiceCommentService.getPostVoiceComments(postId)).thenReturn(voiceComments);

        //when
        PaginationResponse<PostCommentResponse> responses = commentController
                .getPostComments(postId, page, size, "ASC", "1");

        //then
        assertEquals(comments.size()+voiceComments.size(), responses.results().size());
        assertEquals(page, responses.currentPage());
        assertEquals(size, responses.itemPerPage());
    }
}
