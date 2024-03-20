package com.oing.service;

import com.oing.domain.Post;
import com.oing.domain.RealEmoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.exception.RegisteredRealEmojiNotFoundException;
import com.oing.repository.RealEmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealEmojiService {
    private final RealEmojiRepository realEmojiRepository;

    /**
     * 게시물에 리얼 이모지를 저장합니다
     * @param postRealEmoji 리얼 이모지
     * @return 저장된 리얼 이모지
     */
    public RealEmoji savePostRealEmoji(RealEmoji postRealEmoji) {
        return realEmojiRepository.save(postRealEmoji);
    }

    /**
     * 게시물에 등록된 리얼 이모지가 있는지 조회
     * @param post 조회할 포스트
     * @param memberId 회원 아이디
     * @param realEmoji 조회 대상 리얼 이모지
     * @return 존재 여부
     */
    public boolean isMemberPostRealEmojiExists(Post post, String memberId, MemberRealEmoji realEmoji) {
        return realEmojiRepository.existsByPostAndMemberIdAndRealEmoji(post, memberId, realEmoji);
    }

    /**
     * 게시물에 등록된 리얼 이모지를 반환
     * @param realEmojiId 리얼 이모지 아이디
     * @param memberId 회원 아이디
     * @return 게시물에 등록된 리얼 이모지
     * @throws RegisteredRealEmojiNotFoundException 등록된 리얼 이모지가 없는 경우
     */
    public RealEmoji getMemberPostRealEmojiByRealEmojiIdAndMemberIdAndPostId(String realEmojiId, String memberId,
                                                                             String postId) {
        return realEmojiRepository.findByRealEmojiIdAndMemberIdAndPostId(realEmojiId, memberId, postId)
                .orElseThrow(RegisteredRealEmojiNotFoundException::new);
    }

    /**
     * 게시물에 등록된 리얼 이모지를 삭제
     * @param postRealEmoji 리얼 이모지
     */
    public void deletePostRealEmoji(RealEmoji postRealEmoji) {
        realEmojiRepository.delete(postRealEmoji);
    }
}
