package com.oing.service;

import com.oing.domain.Post;
import com.oing.domain.VoiceComment;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.MemberVoiceCommentNotFoundException;
import com.oing.repository.VoiceCommentRepository;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoiceCommentService {

    private final VoiceCommentRepository voiceCommentRepository;
    private final IdentityGenerator identityGenerator;
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final MemberBridge memberBridge;

    @Transactional
    public PreSignedUrlResponse requestPresignedUrl(String loginMemberId, String fileName) {
        log.info("Member {} is trying to request voice-comment Pre-Signed URL", loginMemberId);

        PreSignedUrlResponse response = preSignedUrlGenerator.getVoiceCommentPreSignedUrl(fileName);
        log.info("Voice-comment Pre-Signed URL has been generated for member {}: {}", loginMemberId, response.url());
        return response;
    }

    @Transactional
    public VoiceComment saveVoiceComment(Post post, CreatePostCommentRequest request, String loginMemberId) {
        validateFamilyMember(loginMemberId, post);

        VoiceComment voiceComment = new VoiceComment(
                identityGenerator.generateIdentity(),
                post,
                loginMemberId,
                request.content()
        );
        VoiceComment savedVoiceComment = voiceCommentRepository.save(voiceComment);
        post.addVoiceComment(savedVoiceComment);
        return savedVoiceComment;
    }

    @Transactional
    public void deleteVoiceComment(Post post, String commentId, String loginMemberId) {
        VoiceComment voiceComment = getMemberVoiceComment(post.getId(), commentId);
        validateVoiceCommentOwner(loginMemberId, voiceComment);

        voiceCommentRepository.delete(voiceComment);
        post.removeVoiceComment(voiceComment);
    }

    public VoiceComment getMemberVoiceComment(String postId, String commentId) {
        VoiceComment voiceComment = voiceCommentRepository
                .findById(commentId)
                .orElseThrow(MemberVoiceCommentNotFoundException::new);

        if (!voiceComment.getPost().getId().equals(postId)) throw new MemberVoiceCommentNotFoundException();
        return voiceComment;
    }

    private void validateFamilyMember(String memberId, Post post) {
        if (!memberBridge.isInSameFamily(memberId, post.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting voice-comment operation on post {}", memberId, post.getId());
            throw new AuthorizationFailedException();
        }
    }

    private void validateVoiceCommentOwner(String memberId, VoiceComment voiceComment) {
        if (!voiceComment.getMemberId().equals(memberId)) {
            log.warn("Unauthorized access attempt: Member {} is attempting voice-comment operation on post {}", memberId, voiceComment.getPost().getId());
            throw new AuthorizationFailedException();
        }
    }

    public List<VoiceComment> getPostVoiceComments(String postId) {
        return voiceCommentRepository.findByPostId(postId);
    }

    public long countMonthlyVoiceCommentByMemberId(LocalDate date, String memberId) {
        int year = date.getYear();
        int month = date.getMonthValue();

        return voiceCommentRepository.countMonthlyVoiceCommentByMemberId(year, month, memberId);
    }
}
