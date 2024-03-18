package com.oing.service;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostComment;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.MemberPostCommentNotFoundException;
import com.oing.repository.MemberPostCommentRepository;
import com.oing.service.event.DeleteMemberPostEvent;
import com.oing.util.IdentityGenerator;
import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberPostCommentService {
    private final MemberPostCommentRepository memberPostCommentRepository;
    private final MemberBridge memberBridge;
    private final IdentityGenerator identityGenerator;


    public MemberPostComment savePostComment(MemberPost post, CreatePostCommentRequest request, String loginMemberId) {
        validateFamilyMember(loginMemberId, post);

        MemberPostComment memberPostComment = new MemberPostComment(
                identityGenerator.generateIdentity(),
                post,
                loginMemberId,
                request.content()
        );
        MemberPostComment savedComment = memberPostCommentRepository.save(memberPostComment);
        post.addComment(savedComment);
        return savedComment;
    }

    public void deletePostComment(MemberPost post, String commentId, String loginMemberId) {
        MemberPostComment memberPostComment = getMemberPostComment(post.getId(), commentId);
        validateCommentOwner(loginMemberId, memberPostComment);

        memberPostCommentRepository.delete(memberPostComment);
        post.removeComment(memberPostComment);
    }

    public MemberPostComment updateMemberPostComment(String postId, String commentId, String content, String loginMemberId) {
        MemberPostComment memberPostComment = getMemberPostComment(postId, commentId);
        validateCommentOwner(loginMemberId, memberPostComment);

        memberPostComment.setContent(content);
        return memberPostComment;
    }

    public MemberPostComment getMemberPostComment(String postId, String commentId) {
        MemberPostComment memberPostComment = memberPostCommentRepository
                .findById(commentId)
                .orElseThrow(MemberPostCommentNotFoundException::new);

        if (!memberPostComment.getPost().getId().equals(postId)) throw new MemberPostCommentNotFoundException();
        return memberPostComment;
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

    private void validateFamilyMember(String memberId, MemberPost post) {
        if (!memberBridge.isInSameFamily(memberId, post.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting reaction operation on post {}", memberId, post.getId());
            throw new AuthorizationFailedException();
        }
    }

    private void validateCommentOwner(String memberId, MemberPostComment comment) {
        if (!comment.getMemberId().equals(memberId)) {
            log.warn("Unauthorized access attempt: Member {} is attempting comment operation on post {}", memberId, comment.getPost().getId());
            throw new AuthorizationFailedException();
        }
    }

    @EventListener
    public void deleteAllWhenPostDelete(DeleteMemberPostEvent event) {
        MemberPost post = event.memberPost();
        memberPostCommentRepository.deleteAllByPostId(post.getId());
    }
}
