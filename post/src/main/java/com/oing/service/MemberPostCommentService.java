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


    @Transactional
    public MemberPostComment savePostComment(MemberPostComment memberPostComment) {
        return memberPostCommentRepository.save(memberPostComment);
    }

    public MemberPostComment getMemberPostComment(String postId, String commentId) {
        MemberPostComment memberPostComment = memberPostCommentRepository
                .findById(commentId)
                .orElseThrow(MemberPostCommentNotFoundException::new);

        if (!memberPostComment.getPost().getId().equals(postId)) throw new MemberPostCommentNotFoundException();
        return memberPostComment;
    }

    @Transactional
    public void deletePostComment(MemberPostComment memberPostComment) {
        memberPostCommentRepository.delete(memberPostComment);
    }

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
