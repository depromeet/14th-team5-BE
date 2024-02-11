package com.oing.service;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostComment;
import com.oing.domain.PaginationDTO;
import com.oing.exception.MemberPostCommentNotFoundException;
import com.oing.repository.MemberPostCommentRepository;
import com.oing.service.event.DeleteMemberPostEvent;
import com.querydsl.core.QueryResults;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class MemberPostCommentService {
    private final MemberPostCommentRepository memberPostCommentRepository;

    /**
     * 게시물의 댓글을 저장합니다
     * @param memberPostComment 댓글
     * @return 저장된 댓글
     */
    @Transactional
    public MemberPostComment savePostComment(MemberPostComment memberPostComment) {
        return memberPostCommentRepository.save(memberPostComment);
    }

    /**
     * 게시물의 댓글을 조회합니다
     * @param postId 게시글 ID
     * @param commentId 댓글 ID
     * @return 댓글
     * @throws MemberPostCommentNotFoundException 댓글이 존재하지 않거나 게시글 ID가 댓글의 ID와 일치하지 않을 경우
     */
    @Transactional
    public MemberPostComment getMemberPostComment(String postId, String commentId) {
        MemberPostComment memberPostComment = memberPostCommentRepository
                .findById(commentId)
                .orElseThrow(MemberPostCommentNotFoundException::new);

        if (!memberPostComment.getPost().getId().equals(postId)) throw new MemberPostCommentNotFoundException();
        return memberPostComment;
    }

    /**
     * 게시물의 댓글을 삭제합니다
     * @param memberPostComment 댓글
     */
    @Transactional
    public void deletePostComment(MemberPostComment memberPostComment) {
        memberPostCommentRepository.delete(memberPostComment);
    }

    /**
     * 게시글의 댓글들을 조회합니다.
     * @param page 페이지
     * @param size 페이지당 댓글 수
     * @param postId 게시글 ID
     * @param asc 오름차순 여부
     * @return 댓글들 조회 결과
     */
    @Transactional
    public PaginationDTO<MemberPostComment> searchPostComments(int page, int size, String postId, boolean asc) {
        QueryResults<MemberPostComment> results = memberPostCommentRepository
                .searchPostComments(page, size, postId, asc);
        int totalPage = (int) Math.ceil((double) results.getTotal() / size);
        return new PaginationDTO<>(
                totalPage,
                results.getResults()
        );
    }

    @EventListener
    public void deleteAllWhenPostDelete(DeleteMemberPostEvent event) {
        MemberPost post = event.memberPost();
        memberPostCommentRepository.deleteAllByPostId(post.getId());
    }
}
