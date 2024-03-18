package com.oing.service;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostComment;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.MemberPostCommentNotFoundException;
import com.oing.repository.MemberPostCommentRepository;
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
public class MemberPostCommentServiceTest {
    @InjectMocks
    private MemberPostCommentService memberPostCommentService;

    @Mock
    private MemberPostCommentRepository memberPostCommentRepository;
    @Mock
    private MemberBridge memberBridge;
    @Mock
    private IdentityGenerator identityGenerator;


    @Test
    void 게시물_댓글_저장_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1", "1");
        CreatePostCommentRequest request = new CreatePostCommentRequest("1");
        MemberPostComment memberPostComment = new MemberPostComment("1", null, "1", "1");
        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(true);
        when(identityGenerator.generateIdentity()).thenReturn("1");
        when(memberPostCommentRepository.save(any())).thenReturn(memberPostComment);

        //when
        MemberPostComment memberPostComment1 = memberPostCommentService.savePostComment(post, request, memberId);

        //then
        assertEquals(memberPostComment, memberPostComment1);
    }

    @Test
    void 게시물_댓글_생성_내_가족이_아닌_경우_테스트() {
        //given
        MemberPost memberPost = new MemberPost("1", "1", "1", "1", "1", "1");
        MemberPostComment memberPostComment = spy(new MemberPostComment("1", memberPost, "1", "1"));
        CreatePostCommentRequest request = new CreatePostCommentRequest(memberPostComment.getComment());

        //when
        //then
        assertThrows(AuthorizationFailedException.class,
                () -> memberPostCommentService.savePostComment(memberPost, request, "1")
        );
    }

    @Test
    void 게시물_삭제_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1", "1");
        when(memberPostCommentRepository.findById("1")).thenReturn(Optional.of(new MemberPostComment("1", post, "1", "1")));

        //when
        memberPostCommentService.deletePostComment(post, "1", memberId);

        //then
        //ignore if no exception
    }

    @Test
    void 게시물_댓글_삭제_내가_작성하지_않은경우_테스트() {
        //given
        MemberPost memberPost = new MemberPost("1", "1", "1", "1", "1", "1");
        MemberPostComment othersMemberPostComment = new MemberPostComment("1", memberPost, "2", "1");
        when(memberPostCommentRepository.findById("1")).thenReturn(Optional.of(othersMemberPostComment));

        //when
        //then
        assertThrows(AuthorizationFailedException.class,
                () -> memberPostCommentService.deletePostComment(memberPost, othersMemberPostComment.getId(), "1")
        );
    }

    @Test
    void 게시물_댓글_수정_테스트() {
        //given
        MemberPost memberPost = new MemberPost("1", "1", "1", "1", "1", "1");
        MemberPostComment memberPostComment = spy(new MemberPostComment("1", memberPost, "1", "1"));
        UpdatePostCommentRequest updateRequest = new UpdatePostCommentRequest(memberPostComment.getComment());
        when(memberPostCommentRepository.findById("1")).thenReturn(Optional.of(memberPostComment));

        //when
        MemberPostComment updatedComment = memberPostCommentService.updateMemberPostComment(
                memberPost.getId(),
                memberPostComment.getId(),
                updateRequest.content(),
                "1"
        );

        //then
        assertEquals(updateRequest.content(), updatedComment.getComment());
    }

    @Test
    void 게시물_댓글_수정_내가_작성하지_않은경우_테스트() {
        //given
        MemberPost memberPost = new MemberPost("1", "1", "1", "1", "1", "1");
        MemberPostComment othersMemberPostComment = new MemberPostComment("1", memberPost, "2", "1");
        UpdatePostCommentRequest request = new UpdatePostCommentRequest(othersMemberPostComment.getComment());
        when(memberPostCommentRepository.findById("1")).thenReturn(Optional.of(othersMemberPostComment));

        //when
        //then
        assertThrows(AuthorizationFailedException.class, () ->
            memberPostCommentService.updateMemberPostComment(memberPost.getId(), othersMemberPostComment.getId(), request.content(), "1")
        );
    }

    @Test
    void 게시물_댓글_조회_테스트() {
        //given
        MemberPost memberPost = new MemberPost("1", "1", "1", "1", "1", "1");
        MemberPostComment memberPostComment = new MemberPostComment("1", memberPost, "1", "1");
        when(memberPostCommentRepository.findById("1")).thenReturn(java.util.Optional.of(memberPostComment));

        //when
        MemberPostComment memberPostComment1 = memberPostCommentService
                .getMemberPostComment("1", "1");

        //then
        assertEquals(memberPostComment, memberPostComment1);
    }

    @Test
    void 게시물_댓글_조회_게시물ID_댓글ID_불일치_테스트() {
        //given
        MemberPost memberPost = new MemberPost("1", "1", "1", "1", "1", "1");
        MemberPostComment memberPostComment = new MemberPostComment("1", memberPost, "1", "1");
        when(memberPostCommentRepository.findById("2")).thenReturn(java.util.Optional.of(memberPostComment));

        //when
        //then
        assertThrows(MemberPostCommentNotFoundException.class, () -> {
            memberPostCommentService.getMemberPostComment("2", "2");
        });
    }

    @Test
    void 게시물_댓글_조회_댓글_못찾음_테스트() {
        //given
        when(memberPostCommentRepository.findById("1")).thenReturn(java.util.Optional.empty());

        //when
        //then
        assertThrows(MemberPostCommentNotFoundException.class, () -> {
            memberPostCommentService.getMemberPostComment("1", "1");
        });
    }

    @Test
    void 게시물_댓글_검색_테스트() {
        //given
        MemberPost memberPost = new MemberPost(
                "1",
                "1",
                "1",
                "1",
                "1",
                "1"
        );
        MemberPostComment memberPostComment = new MemberPostComment(
                "1",
                memberPost,
                "1",
                "1"
        );
        int page = 1;
        int size = 5;
        when(memberPostCommentRepository.searchPostComments(
                page,
                size,
                memberPost.getId(),
                true
        )).thenReturn(new QueryResults<>(Lists.newArrayList(memberPostComment), (long)size, 1L, 1L));

        //when
        PaginationDTO<MemberPostComment> memberPostComment1 = memberPostCommentService
                .searchPostComments(page, size, memberPost.getId(), true);

        //then
        //nothing
    }
}
