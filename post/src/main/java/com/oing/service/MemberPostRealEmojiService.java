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

    /**
     * 게시물에 리얼 이모지를 등록합니다
     * @param request 리얼 이모지 등록 요청
     * @param loginMemberId 로그인한 회원 아이디
     * @param loginFamilyId 로그인한 가족 아이디
     * @param post 게시물
     * @return 게시물에 등록된 리얼 이모지
     */
    public MemberPostRealEmoji createPostRealEmoji(
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

    /**
     * 게시물에 등록된 리얼 이모지가 있는지 조회
     * @param post 조회할 포스트
     * @param memberId 회원 아이디
     * @param realEmoji 조회 대상 리얼 이모지
     * @return 존재 여부
     */
    public boolean isMemberPostRealEmojiExists(MemberPost post, String memberId, MemberRealEmoji realEmoji) {
        return memberPostRealEmojiRepository.existsByPostAndMemberIdAndRealEmoji(post, memberId, realEmoji);
    }

    private void validatePostRealEmojiForAddition(MemberPost post, String memberId, MemberRealEmoji emoji) {
        if (isMemberPostRealEmojiExists(post, memberId, emoji)) {
            throw new RealEmojiAlreadyExistsException();
        }
    }

    public MemberRealEmoji getMemberRealEmojiByIdAndFamilyId(String realEmojiId, String familyId) {
        return memberRealEmojiRepository
                .findByIdAndFamilyId(realEmojiId, familyId)
                .orElseThrow(RealEmojiNotFoundException::new);
    }

    /**
     * 게시물에 등록된 리얼 이모지를 반환
     * @param realEmojiId 리얼 이모지 아이디
     * @param memberId 회원 아이디
     * @return 게시물에 등록된 리얼 이모지
     * @throws RegisteredRealEmojiNotFoundException 등록된 리얼 이모지가 없는 경우
     */
    public MemberPostRealEmoji getMemberPostRealEmojiByRealEmojiIdAndMemberIdAndPostId(String realEmojiId, String memberId,
                                                                                       String postId) {
        return memberPostRealEmojiRepository.findByRealEmojiIdAndMemberIdAndPostId(realEmojiId, memberId, postId)
                .orElseThrow(RegisteredRealEmojiNotFoundException::new);
    }

    /**
     * 게시물에 등록된 리얼 이모지를 삭제
     * @param postRealEmoji 리얼 이모지
     */
    public void deletePostRealEmoji(MemberPostRealEmoji postRealEmoji) {
        memberPostRealEmojiRepository.delete(postRealEmoji);
    }
}
