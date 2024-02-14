package com.oing.repository;

import com.oing.domain.Emoji;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostReaction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface MemberPostReactionRepository extends JpaRepository<MemberPostReaction, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByPostAndMemberIdAndEmoji(MemberPost post, String memberId, Emoji emoji);

    Optional<MemberPostReaction> findReactionByPostAndMemberIdAndEmoji(MemberPost post, String memberId, Emoji emoji);

    List<MemberPostReaction> findAllByPostId(String postId);

    void deleteAllByPostId(String memberPostId);
}
