package com.oing.service;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostComment;
import com.oing.domain.PaginationDTO;
import com.oing.exception.MemberPostCommentNotFoundException;
import com.oing.repository.MemberPostCommentRepository;
import com.querydsl.core.QueryResults;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberPostCommentServiceTest {
    @InjectMocks
    private MemberPostCommentService memberPostCommentService;

    @Mock
    private MemberPostCommentRepository memberPostCommentRepository;

    @Test
    void 게시물_저장_테스트() {
        //given
        MemberPostComment memberPostComment = new MemberPostComment(
                "1",
                null,
                "1",
                "1"
        );
        when(memberPostCommentRepository.save(any())).thenReturn(memberPostComment);

        //when
        MemberPostComment memberPostComment1 = memberPostCommentService.savePostComment(memberPostComment);

        //then
        assertEquals(memberPostComment, memberPostComment1);
    }

    @Test
    void 게시물_댓글_조회_테스트() {
        //given
        MemberPost memberPost = new MemberPost(
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
        MemberPost memberPost = new MemberPost(
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
    void 게시물_삭제_테스트() {
        //given
        MemberPostComment memberPostComment = new MemberPostComment(
                "1",
                null,
                "1",
                "1"
        );
        doNothing().when(memberPostCommentRepository).delete(any());

        //when
        memberPostCommentService.deletePostComment(memberPostComment);

        //then
        //ignore if no exception
    }

    @Test
    void 게시물_댓글_검색_테스트() {
        //given
        MemberPost memberPost = new MemberPost(
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
