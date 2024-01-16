package com.oing.service;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostRealEmoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.exception.RegisteredRealEmojiNotFoundException;
import com.oing.repository.MemberPostRealEmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberPostRealEmojiService {
    private final MemberPostRealEmojiRepository memberPostRealEmojiRepository;

    /**
     * 게시물에 리얼 이모지를 저장합니다
     * @param postRealEmoji 리얼 이모지
     * @return 저장된 리얼 이모지
     */
    public MemberPostRealEmoji savePostRealEmoji(MemberPostRealEmoji postRealEmoji) {
        return memberPostRealEmojiRepository.save(postRealEmoji);
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

    /**
     * 게시물에 등록된 리얼 이모지를 반환
     * @param realEmojiId 리얼 이모지 아이디
     * @param memberId 회원 아이디
     * @return 게시물에 등록된 리얼 이모지
     * @throws RegisteredRealEmojiNotFoundException 등록된 리얼 이모지가 없는 경우
     */
    public MemberPostRealEmoji getMemberPostRealEmojiByRealEmojiIdAndMemberId(String realEmojiId, String memberId) {
        return memberPostRealEmojiRepository.findByRealEmojiIdAndMemberId(realEmojiId, memberId)
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
