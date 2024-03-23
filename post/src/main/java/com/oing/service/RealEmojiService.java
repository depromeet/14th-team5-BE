package com.oing.service;

import com.oing.domain.MemberRealEmoji;
import com.oing.domain.Post;
import com.oing.domain.RealEmoji;
import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.RealEmojiAlreadyExistsException;
import com.oing.exception.RealEmojiNotFoundException;
import com.oing.exception.RegisteredRealEmojiNotFoundException;
import com.oing.repository.MemberRealEmojiRepository;
import com.oing.repository.RealEmojiRepository;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealEmojiService {
    private final RealEmojiRepository realEmojiRepository;
    private final MemberRealEmojiRepository memberRealEmojiRepository;
    private final IdentityGenerator identityGenerator;
    private final MemberBridge memberBridge;


    @Transactional
    public RealEmoji registerRealEmojiAtPost(
            PostRealEmojiRequest request, String loginMemberId, String loginFamilyId, Post post) {
        validateFamilyMember(loginMemberId, post);
        MemberRealEmoji realEmoji = getMemberRealEmojiByIdAndFamilyId(request.realEmojiId(), loginFamilyId);
        validatePostRealEmojiForAddition(post, loginMemberId, realEmoji);

        RealEmoji postRealEmoji = new RealEmoji(identityGenerator.generateIdentity(), realEmoji,
                post, loginMemberId);
        post.addRealEmoji(postRealEmoji);
        return realEmojiRepository.save(postRealEmoji);
    }

    private void validateFamilyMember(String memberId, Post post) {
        if (!memberBridge.isInSameFamily(memberId, post.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting to access post real emoji", memberId);
            throw new AuthorizationFailedException();
        }
    }

    private void validatePostRealEmojiForAddition(Post post, String memberId, MemberRealEmoji emoji) {
        if (isMemberPostRealEmojiExists(post, memberId, emoji)) {
            throw new RealEmojiAlreadyExistsException();
        }
    }

    public boolean isMemberPostRealEmojiExists(Post post, String memberId, MemberRealEmoji realEmoji) {
        return realEmojiRepository.existsByPostAndMemberIdAndRealEmoji(post, memberId, realEmoji);
    }

    public MemberRealEmoji getMemberRealEmojiByIdAndFamilyId(String realEmojiId, String familyId) {
        return memberRealEmojiRepository
                .findByIdAndFamilyId(realEmojiId, familyId)
                .orElseThrow(RealEmojiNotFoundException::new);
    }

    public RealEmoji getMemberPostRealEmojiByRealEmojiIdAndMemberIdAndPostId(String realEmojiId, String memberId,
                                                                                       String postId) {
        return realEmojiRepository.findByRealEmojiIdAndMemberIdAndPostId(realEmojiId, memberId, postId)
                .orElseThrow(RegisteredRealEmojiNotFoundException::new);
    }

    public void deletePostRealEmoji(RealEmoji postRealEmoji) {
        realEmojiRepository.delete(postRealEmoji);
    }
}
