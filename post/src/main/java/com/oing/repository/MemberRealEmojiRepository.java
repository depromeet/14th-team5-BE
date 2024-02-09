package com.oing.repository;

import com.oing.domain.Emoji;
import com.oing.domain.MemberRealEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRealEmojiRepository extends JpaRepository<MemberRealEmoji, String> {

    Optional<MemberRealEmoji> findByIdAndFamilyId(String realEmojiId, String familyId);

    Optional<MemberRealEmoji> findByTypeAndMemberIdAndFamilyId(Emoji emoji, String memberId, String familyId);

    List<MemberRealEmoji> findAllByMemberIdAndFamilyId(String memberId, String familyId);
}
