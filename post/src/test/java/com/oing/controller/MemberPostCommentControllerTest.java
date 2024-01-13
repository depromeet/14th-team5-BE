package com.oing.controller;

import com.google.common.collect.Lists;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostComment;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostCommentResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.service.MemberBridge;
import com.oing.service.MemberPostCommentService;
import com.oing.service.MemberPostService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberPostCommentControllerTest {
    @InjectMocks
    private MemberPostCommentController memberPostCommentController;

    @Mock
    private AuthenticationHolder authenticationHolder;
    @Mock
    private IdentityGenerator identityGenerator;
    @Mock
    private MemberPostService memberPostService;
    @Mock
    private MemberPostCommentService memberPostCommentService;
    @Mock
    private MemberBridge memberBridge;

    @Test
    void 게시물_댓글_생성_테스트() {
        //given
        MemberPost memberPost = new MemberPost(
                "1",
                "1",
                "1",
                "1",
                "1"
        );
        MemberPostComment memberPostComment = spy(new MemberPostComment(
                "1",
                memberPost,
                "1",
                "1"
        ));
        CreatePostCommentRequest request = new CreatePostCommentRequest(memberPostComment.getComment());
        when(memberPostService.getMemberPostById("1")).thenReturn(memberPost);
        when(authenticationHolder.getUserId()).thenReturn("1");
        when(memberBridge.isInSameFamily("1", "1")).thenReturn(true);
        when(identityGenerator.generateIdentity()).thenReturn(memberPost.getId());
        when(memberPostCommentService.savePostComment(any())).thenReturn(memberPostComment);
        when(memberPostComment.getCreatedAt()).thenReturn(LocalDateTime.now());

        //when
        PostCommentResponse response = memberPostCommentController.createPostComment(
                memberPost.getId(),
                request
        );

        //then
        assertEquals(response.comment(), request.content());
    }

    @Test
    void 게시물_댓글_생성_내_가족이_아닌_경우_테스트() {
        //given
        MemberPost memberPost = new MemberPost(
                "1",
                "1",
                "1",
                "1",
                "1"
        );
        MemberPostComment memberPostComment = spy(new MemberPostComment(
                "1",
                memberPost,
                "1",
                "1"
        ));
        CreatePostCommentRequest request = new CreatePostCommentRequest(memberPostComment.getComment());
        when(memberPostService.getMemberPostById("1")).thenReturn(memberPost);
        when(authenticationHolder.getUserId()).thenReturn("1");
        when(memberBridge.isInSameFamily("1", "1")).thenReturn(false);

        //when
        //then
        assertThrows(AuthorizationFailedException.class, () -> {
            memberPostCommentController.createPostComment(
                    memberPost.getId(),
                    request
            );
        });
    }

    @Test
    void 게시물_댓글_삭제_테스트() {
        //given
        MemberPost memberPost = new MemberPost(
                "1",
                "1",
                "1",
                "1",
                "1"
        );
        MemberPostComment memberPostComment = spy(new MemberPostComment(
                "1",
                memberPost,
                "1",
                "1"
        ));
        when(memberPostCommentService.getMemberPostComment(memberPost.getId(), memberPostComment.getId()))
                .thenReturn(memberPostComment);
        when(authenticationHolder.getUserId()).thenReturn("1");

        //when
        memberPostCommentController.deletePostComment(
                memberPost.getId(),
                memberPostComment.getId()
        );

        //then
        //nothing. just check no exception
    }

    @Test
    void 게시물_댓글_삭제_내가_작성하지_않은경우_테스트() {
        //given
        MemberPost memberPost = new MemberPost(
                "1",
                "1",
                "1",
                "1",
                "1"
        );
        MemberPostComment othersMemberPostComment = new MemberPostComment(
                "1",
                memberPost,
                "2",
                "1"
        );
        when(memberPostCommentService.getMemberPostComment(memberPost.getId(), othersMemberPostComment.getId()))
                .thenReturn(othersMemberPostComment);
        when(authenticationHolder.getUserId()).thenReturn("1");

        //when
        //then
        assertThrows(AuthorizationFailedException.class, () -> {
            memberPostCommentController.deletePostComment(
                    memberPost.getId(),
                    othersMemberPostComment.getId()
            );
        });
    }

    @Test
    void 게시물_댓글_수정_테스트() {
        //given
        MemberPost memberPost = new MemberPost(
                "1",
                "1",
                "1",
                "1",
                "1"
        );
        MemberPostComment memberPostComment = spy(new MemberPostComment(
                "1",
                memberPost,
                "1",
                "1"
        ));
        UpdatePostCommentRequest request = new UpdatePostCommentRequest(memberPostComment.getComment());
        when(memberPostCommentService.getMemberPostComment(memberPost.getId(), memberPostComment.getId()))
                .thenReturn(memberPostComment);
        when(authenticationHolder.getUserId()).thenReturn("1");
        when(memberPostCommentService.savePostComment(any())).thenReturn(memberPostComment);
        when(memberPostComment.getCreatedAt()).thenReturn(LocalDateTime.now());

        //when
        PostCommentResponse response = memberPostCommentController.updatePostComment(
                memberPost.getId(),
                memberPostComment.getId(),
                request
        );

        //then
        assertEquals(response.comment(), request.content());
    }

    @Test
    void 게시물_댓글_수정_내가_작성하지_않은경우_테스트() {
        //given
        MemberPost memberPost = new MemberPost(
                "1",
                "1",
                "1",
                "1",
                "1"
        );
        MemberPostComment othersMemberPostComment = new MemberPostComment(
                "1",
                memberPost,
                "2",
                "1"
        );
        UpdatePostCommentRequest request = new UpdatePostCommentRequest(othersMemberPostComment.getComment());
        when(memberPostCommentService.getMemberPostComment(memberPost.getId(), othersMemberPostComment.getId()))
                .thenReturn(othersMemberPostComment);
        when(authenticationHolder.getUserId()).thenReturn("1");

        //when
        //then
        assertThrows(AuthorizationFailedException.class, () -> {
            memberPostCommentController.updatePostComment(
                    memberPost.getId(),
                    othersMemberPostComment.getId(),
                    request
            );
        });
    }

    @Test
    void 게시물_댓글_목록_조회_테스트() {
        //given
        MemberPost memberPost = new MemberPost(
                "1",
                "1",
                "1",
                "1",
                "1"
        );
        MemberPostComment memberPostComment = spy(new MemberPostComment(
                "1",
                memberPost,
                "1",
                "1"
        ));
        int page = 1;
        int size = 10;
        String postId = "1";
        boolean asc = true;
        List<MemberPostComment> memberPostComments = Lists.newArrayList(memberPostComment);

        when(memberPostComment.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(memberPostCommentService.searchPostComments(page, size, postId, asc))
                .thenReturn(new PaginationDTO(
                        5,
                        memberPostComments
                ));

        //when
        PaginationResponse<PostCommentResponse> responses = memberPostCommentController
                .getPostComments(postId, page, size, "ASC");

        //then
        assertEquals(responses.results().size(), memberPostComments.size());
        assertEquals(responses.currentPage(), page);
        assertEquals(responses.itemPerPage(), size);
    }
}
