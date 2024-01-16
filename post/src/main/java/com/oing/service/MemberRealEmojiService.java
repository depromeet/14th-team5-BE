package com.oing.service;

import com.oing.domain.MemberRealEmoji;
import com.oing.exception.RealEmojiNotFoundException;
import com.oing.repository.MemberRealEmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberRealEmojiService {

    private final MemberRealEmojiRepository memberRealEmojiRepository;

    public MemberRealEmoji getMemberRealEmojiById(String realEmojiId) {
        return memberRealEmojiRepository.findById(realEmojiId)
                .orElseThrow(RealEmojiNotFoundException::new);
    }
}
