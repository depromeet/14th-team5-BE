package com.oing.repository;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostRealEmoji;
import com.oing.domain.MemberRealEmoji;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface MemberPostRealEmojiRepository extends JpaRepository<MemberPostRealEmoji, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByPostAndMemberIdAndRealEmoji(MemberPost post, String memberId, MemberRealEmoji emoji);

    Optional<MemberPostRealEmoji> findByRealEmojiIdAndMemberIdAndPostId(String realEmojiId, String memberId, String postId);
}
