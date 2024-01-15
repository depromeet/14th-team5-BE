package com.oing.service;

import com.oing.domain.Emoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.exception.RealEmojiNotFoundException;
import com.oing.repository.MemberRealEmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberRealEmojiService {

    private final MemberRealEmojiRepository memberRealEmojiRepository;

    public MemberRealEmoji save(MemberRealEmoji emoji) {
        return memberRealEmojiRepository.save(emoji);
    }

    public MemberRealEmoji findRealEmojiById(String realEmojiId) {
        return memberRealEmojiRepository
                .findById(realEmojiId)
                .orElseThrow(RealEmojiNotFoundException::new);
    }

    public boolean findRealEmojiByEmojiType(Emoji emoji) {
        return memberRealEmojiRepository
                .findByType(emoji)
                .isPresent();
    }
}
