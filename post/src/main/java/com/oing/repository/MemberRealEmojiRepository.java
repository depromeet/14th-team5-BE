package com.oing.repository;

import com.oing.domain.Emoji;
import com.oing.domain.MemberRealEmoji;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface MemberRealEmojiRepository extends JpaRepository<MemberRealEmoji, String> {

    Optional<MemberRealEmoji> findByIdAndFamilyId(String realEmojiId, String familyId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MemberRealEmoji> findByTypeAndMemberIdAndFamilyId(Emoji emoji, String memberId, String familyId);

    List<MemberRealEmoji> findAllByMemberIdAndFamilyId(String memberId, String familyId);
}
