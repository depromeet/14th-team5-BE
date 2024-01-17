package com.oing.repository;

import com.oing.domain.Emoji;
import com.oing.domain.MemberRealEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRealEmojiRepository extends JpaRepository<MemberRealEmoji, String> {

    Optional<MemberRealEmoji> findByType(Emoji emoji);
}
