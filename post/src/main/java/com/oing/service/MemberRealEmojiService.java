package com.oing.service;

import com.oing.domain.Emoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.exception.RealEmojiNotFoundException;
import com.oing.repository.MemberRealEmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberRealEmojiService {

    private final MemberRealEmojiRepository memberRealEmojiRepository;


    public MemberRealEmoji getMemberRealEmojiByIdAndFamilyId(String realEmojiId, String familyId) {
        return memberRealEmojiRepository
                .findByIdAndFamilyId(realEmojiId, familyId)
                .orElseThrow(RealEmojiNotFoundException::new);
    }

    public MemberRealEmoji save(MemberRealEmoji emoji) {
        return memberRealEmojiRepository.save(emoji);
    }

    public boolean findRealEmojiByEmojiTypeAndMemberIdAndFamilyId(Emoji emoji, String memberId, String familyId) {
        return memberRealEmojiRepository
                .findByTypeAndMemberIdAndFamilyId(emoji, memberId, familyId)
                .isPresent();
    }

    public List<MemberRealEmoji> findRealEmojisByMemberIdAndFamilyId(String memberId, String familyId) {
        return memberRealEmojiRepository.findAllByMemberIdAndFamilyId(memberId, familyId);
    }

}
