package com.oing.service;

import com.oing.domain.Comment;
import com.oing.domain.Post;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.MemberPostCommentNotFoundException;
import com.oing.repository.CommentRepository;
import com.oing.service.event.DeletePostEvent;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberBridge memberBridge;
    private final IdentityGenerator identityGenerator;


    @Transactional
    public Comment savePostComment(Post post, CreatePostCommentRequest request, String loginMemberId) {
        validateFamilyMember(loginMemberId, post);

        Comment comment = new Comment(
                identityGenerator.generateIdentity(),
                post,
                loginMemberId,
                request.content()
        );
        Comment savedComment = commentRepository.save(comment);
        post.addComment(savedComment);
        return savedComment;
    }

    @Transactional
    public void deletePostComment(Post post, String commentId, String loginMemberId) {
        Comment comment = getMemberPostComment(post.getId(), commentId);
        validateCommentOwner(loginMemberId, comment);

        commentRepository.delete(comment);
        post.removeComment(comment);
    }

    @Transactional
    public Comment updateMemberPostComment(String postId, String commentId, String content, String loginMemberId) {
        Comment comment = getMemberPostComment(postId, commentId);
        validateCommentOwner(loginMemberId, comment);

        comment.setContent(content);
        return comment;
    }

    public Comment getMemberPostComment(String postId, String commentId) {
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(MemberPostCommentNotFoundException::new);

        if (!comment.getPost().getId().equals(postId)) throw new MemberPostCommentNotFoundException();
        return comment;
    }

    private void validateFamilyMember(String memberId, Post post) {
        if (!memberBridge.isInSameFamily(memberId, post.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting comment operation on post {}", memberId, post.getId());
            throw new AuthorizationFailedException();
        }
    }

    private void validateCommentOwner(String memberId, Comment comment) {
        if (!comment.getMemberId().equals(memberId)) {
            log.warn("Unauthorized access attempt: Member {} is attempting comment operation on post {}", memberId, comment.getPost().getId());
            throw new AuthorizationFailedException();
        }
    }

    public long countMonthlyCommentByMemberId(LocalDate date, String memberId) {
        int year = date.getYear();
        int month = date.getMonthValue();

        return commentRepository.countMonthlyCommentByMemberId(year, month, memberId);
    }

    @EventListener
    public void deleteAllWhenPostDelete(DeletePostEvent event) {
        Post post = event.post();
        commentRepository.deleteAllByPostId(post.getId());
    }

    public List<Comment> getPostComments(String postId) {
        return commentRepository.findByPostId(postId);
    }
}
