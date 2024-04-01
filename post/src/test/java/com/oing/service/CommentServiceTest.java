package com.oing.service;

import com.oing.domain.Comment;
import com.oing.domain.Post;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.MemberPostCommentNotFoundException;
import com.oing.repository.CommentRepository;
import com.oing.util.IdentityGenerator;
import com.querydsl.core.QueryResults;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostService postService;
    @Mock
    private MemberBridge memberBridge;
    @Mock
    private IdentityGenerator identityGenerator;


    @Test
    void 게시물_댓글_저장_테스트() {
        //given
        String memberId = "1";
        Post post = new Post("1", memberId, "1", "1", "1", "1");
        CreatePostCommentRequest request = new CreatePostCommentRequest("1");
        Comment comment = new Comment("1", null, "1", "1");
        when(postService.getMemberPostById(post.getId())).thenReturn(post);
        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(true);
        when(identityGenerator.generateIdentity()).thenReturn("1");
        when(commentRepository.save(any())).thenReturn(comment);

        //when
        Comment comment1 = commentService.savePostComment(post.getId(), request, memberId);

        //then
        assertEquals(comment, comment1);
    }

    @Test
    void 게시물_댓글_생성_내_가족이_아닌_경우_테스트() {
        //given
        Post post = new Post("1", "1", "1", "1", "1", "1");
        Comment comment = spy(new Comment("1", post, "1", "1"));
        CreatePostCommentRequest request = new CreatePostCommentRequest(comment.getContent());
        when(postService.getMemberPostById(post.getId())).thenReturn(post);

        //when
        //then
        assertThrows(AuthorizationFailedException.class,
                () -> commentService.savePostComment(post.getId(), request, "1")
        );
    }

    @Test
    void 게시물_삭제_테스트() {
        //given
        String memberId = "1";
        Post post = new Post("1", memberId, "1", "1", "1", "1");
        when(postService.getMemberPostById(post.getId())).thenReturn(post);
        when(commentRepository.findById("1")).thenReturn(Optional.of(new Comment("1", post, "1", "1")));

        //when
        commentService.deletePostComment(post.getId(), "1", memberId);

        //then
        //ignore if no exception
    }

    @Test
    void 게시물_댓글_삭제_내가_작성하지_않은경우_테스트() {
        //given
        Post post = new Post("1", "1", "1", "1", "1", "1");
        Comment othersComment = new Comment("1", post, "2", "1");
        when(postService.getMemberPostById(post.getId())).thenReturn(post);
        when(commentRepository.findById("1")).thenReturn(Optional.of(othersComment));

        //when
        //then
        assertThrows(AuthorizationFailedException.class,
                () -> commentService.deletePostComment(post.getId(), othersComment.getId(), "1")
        );
    }

    @Test
    void 게시물_댓글_수정_테스트() {
        //given
        Post post = new Post("1", "1", "1", "1", "1", "1");
        Comment comment = spy(new Comment("1", post, "1", "1"));
        UpdatePostCommentRequest updateRequest = new UpdatePostCommentRequest(comment.getContent());
        when(commentRepository.findById("1")).thenReturn(Optional.of(comment));

        //when
        Comment updatedComment = commentService.updateMemberPostComment(
                post.getId(),
                comment.getId(),
                updateRequest.content(),
                "1"
        );

        //then
        assertEquals(updateRequest.content(), updatedComment.getContent());
    }

    @Test
    void 게시물_댓글_수정_내가_작성하지_않은경우_테스트() {
        //given
        Post post = new Post("1", "1", "1", "1", "1", "1");
        Comment othersComment = new Comment("1", post, "2", "1");
        UpdatePostCommentRequest request = new UpdatePostCommentRequest(othersComment.getContent());
        when(commentRepository.findById("1")).thenReturn(Optional.of(othersComment));

        //when
        //then
        assertThrows(AuthorizationFailedException.class, () ->
            commentService.updateMemberPostComment(post.getId(), othersComment.getId(), request.content(), "1")
        );
    }

    @Test
    void 게시물_댓글_조회_테스트() {
        //given
        Post post = new Post("1", "1", "1", "1", "1", "1");
        Comment comment = new Comment("1", post, "1", "1");
        when(commentRepository.findById("1")).thenReturn(java.util.Optional.of(comment));

        //when
        Comment comment1 = commentService
                .getMemberPostComment("1", "1");

        //then
        assertEquals(comment, comment1);
    }

    @Test
    void 게시물_댓글_조회_게시물ID_댓글ID_불일치_테스트() {
        //given
        Post post = new Post("1", "1", "1", "1", "1", "1");
        Comment comment = new Comment("1", post, "1", "1");
        when(commentRepository.findById("2")).thenReturn(java.util.Optional.of(comment));

        //when
        //then
        assertThrows(MemberPostCommentNotFoundException.class, () -> {
            commentService.getMemberPostComment("2", "2");
        });
    }

    @Test
    void 게시물_댓글_조회_댓글_못찾음_테스트() {
        //given
        when(commentRepository.findById("1")).thenReturn(java.util.Optional.empty());

        //when
        //then
        assertThrows(MemberPostCommentNotFoundException.class, () -> {
            commentService.getMemberPostComment("1", "1");
        });
    }

    @Test
    void 게시물_댓글_검색_테스트() {
        //given
        Post post = new Post(
                "1",
                "1",
                "1",
                "1",
                "1",
                "1"
        );
        Comment comment = new Comment(
                "1",
                post,
                "1",
                "1"
        );
        int page = 1;
        int size = 5;
        when(commentRepository.searchPostComments(
                page,
                size,
                post.getId(),
                true
        )).thenReturn(new QueryResults<>(Lists.newArrayList(comment), (long)size, 1L, 1L));

        //when
        PaginationDTO<Comment> memberPostComment1 = commentService
                .searchPostComments(page, size, post.getId(), true);

        //then
        //nothing
    }
}
