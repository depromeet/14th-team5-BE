package com.oing.controller;

import com.google.common.collect.Lists;
import com.oing.domain.Comment;
import com.oing.domain.PaginationDTO;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostCommentResponse;
import com.oing.service.CommentService;
import com.oing.service.MemberBridge;
import com.oing.service.PostService;
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
    private MemberBridge memberBridge;

    @Test
    void 게시물_댓글_생성_테스트() {
        //given
        Post post = new Post("1", "1", "1", PostType.SURVIVAL, "1", "1", "1");
        Comment comment = spy(new Comment("1", post, "1", "1"));
        CreatePostCommentRequest request = new CreatePostCommentRequest(comment.getContent());
        when(postService.getMemberPostById("1")).thenReturn(post);
        when(commentService.savePostComment(post, request, "1")).thenReturn(comment);

        //when
        PostCommentResponse response = commentController.createPostComment(
                post.getId(),
                request,
                "1"
        );

        //then
        assertEquals(response.comment(), request.content());
    }


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
    void 게시물_댓글_수정_테스트() {
        //given
        Post post = new Post("1", "1", "1", PostType.SURVIVAL, "1", "1", "1");
        Comment comment = spy(new Comment("1", post, "1", "1"));
        UpdatePostCommentRequest updateRequest = new UpdatePostCommentRequest(comment.getContent());
        when(commentService.updateMemberPostComment(post.getId(), comment.getId(), updateRequest.content(), "1"))
                .thenReturn(comment);

        //when
        PostCommentResponse response = commentController.updatePostComment(
                post.getId(),
                comment.getId(),
                updateRequest,
                "1"
        );

        //then
        assertEquals(response.comment(), updateRequest.content());
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
        int page = 1;
        int size = 10;
        String postId = "1";
        boolean asc = true;
        List<Comment> comments = Lists.newArrayList(comment);
        when(postService.getMemberPostById("1")).thenReturn(post);
        when(memberBridge.isInSameFamily("1", "1")).thenReturn(true);
        when(comment.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(commentService.searchPostComments(page, size, postId, asc))
                .thenReturn(new PaginationDTO(
                        5,
                        comments
                ));

        //when
        PaginationResponse<PostCommentResponse> responses = commentController
                .getPostComments(postId, page, size, "ASC", "1");

        //then
        assertEquals(responses.results().size(), comments.size());
        assertEquals(responses.currentPage(), page);
        assertEquals(responses.itemPerPage(), size);
    }
}
