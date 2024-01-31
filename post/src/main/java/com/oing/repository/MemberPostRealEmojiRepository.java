package com.oing.repository;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostRealEmoji;
import com.oing.domain.MemberRealEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberPostRealEmojiRepository extends JpaRepository<MemberPostRealEmoji, String> {
    boolean existsByPostAndMemberIdAndRealEmoji(MemberPost post, String memberId, MemberRealEmoji emoji);

    Optional<MemberPostRealEmoji> findByRealEmojiIdAndMemberIdAndPostId(String realEmojiId, String memberId, String postId);

    long countByMemberIdInAndCreatedAtBetween(List<String> memberIds, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
