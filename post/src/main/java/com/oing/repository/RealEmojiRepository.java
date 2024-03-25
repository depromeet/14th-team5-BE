package com.oing.repository;

import com.oing.domain.Post;
import com.oing.domain.RealEmoji;
import com.oing.domain.MemberRealEmoji;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface RealEmojiRepository extends JpaRepository<RealEmoji, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByPostAndMemberIdAndRealEmoji(Post post, String memberId, MemberRealEmoji emoji);

    Optional<RealEmoji> findByRealEmojiIdAndMemberIdAndPostId(String realEmojiId, String memberId, String postId);
}
