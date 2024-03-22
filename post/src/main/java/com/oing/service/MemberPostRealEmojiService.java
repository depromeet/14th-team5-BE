package com.oing.service;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostRealEmoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.RealEmojiAlreadyExistsException;
import com.oing.exception.RealEmojiNotFoundException;
import com.oing.exception.RegisteredRealEmojiNotFoundException;
import com.oing.repository.MemberPostRealEmojiRepository;
import com.oing.repository.MemberRealEmojiRepository;
import com.oing.util.IdentityGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberPostRealEmojiService {
    private final MemberPostRealEmojiRepository memberPostRealEmojiRepository;
    private final MemberRealEmojiRepository memberRealEmojiRepository;
    private final IdentityGenerator identityGenerator;
    private final MemberBridge memberBridge;


    public MemberPostRealEmoji registerRealEmojiAtPost(
            PostRealEmojiRequest request, String loginMemberId, String loginFamilyId, MemberPost post) {
        validateFamilyMember(loginMemberId, post);
        MemberRealEmoji realEmoji = getMemberRealEmojiByIdAndFamilyId(request.realEmojiId(), loginFamilyId);
        validatePostRealEmojiForAddition(post, loginMemberId, realEmoji);

        MemberPostRealEmoji postRealEmoji = new MemberPostRealEmoji(identityGenerator.generateIdentity(), realEmoji,
                post, loginMemberId);
        post.addRealEmoji(postRealEmoji);
        return memberPostRealEmojiRepository.save(postRealEmoji);
    }

    private void validateFamilyMember(String memberId, MemberPost post) {
        if (!memberBridge.isInSameFamily(memberId, post.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting to access post real emoji", memberId);
            throw new AuthorizationFailedException();
        }
    }

    private void validatePostRealEmojiForAddition(MemberPost post, String memberId, MemberRealEmoji emoji) {
        if (isMemberPostRealEmojiExists(post, memberId, emoji)) {
            throw new RealEmojiAlreadyExistsException();
        }
    }

    public boolean isMemberPostRealEmojiExists(MemberPost post, String memberId, MemberRealEmoji realEmoji) {
        return memberPostRealEmojiRepository.existsByPostAndMemberIdAndRealEmoji(post, memberId, realEmoji);
    }

    public MemberRealEmoji getMemberRealEmojiByIdAndFamilyId(String realEmojiId, String familyId) {
        return memberRealEmojiRepository
                .findByIdAndFamilyId(realEmojiId, familyId)
                .orElseThrow(RealEmojiNotFoundException::new);
    }

    public MemberPostRealEmoji getMemberPostRealEmojiByRealEmojiIdAndMemberIdAndPostId(String realEmojiId, String memberId,
                                                                                       String postId) {
        return memberPostRealEmojiRepository.findByRealEmojiIdAndMemberIdAndPostId(realEmojiId, memberId, postId)
                .orElseThrow(RegisteredRealEmojiNotFoundException::new);
    }

    public void deletePostRealEmoji(MemberPostRealEmoji postRealEmoji) {
        memberPostRealEmojiRepository.delete(postRealEmoji);
    }
}
