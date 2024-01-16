package com.oing.service;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostRealEmoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.repository.MemberPostRealEmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberPostRealEmojiService {
    private final MemberPostRealEmojiRepository memberPostRealEmojiRepository;

    public void savePostRealEmoji(MemberPostRealEmoji emoji) {
        memberPostRealEmojiRepository.save(emoji);
    }

    public boolean isMemberPostReactionExists(MemberPost post, String memberId, MemberRealEmoji emoji) {
        return memberPostRealEmojiRepository.existsByPostAndMemberIdAndRealEmoji(post, memberId, emoji);
    }
}
