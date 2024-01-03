package com.oing.repository;

import com.oing.domain.Emoji;
import com.oing.domain.model.MemberPost;
import com.oing.domain.model.MemberPostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberPostReactionRepository extends JpaRepository<MemberPostReaction, String> {
    boolean existsByPostAndMemberIdAndEmoji(MemberPost post, String memberId, Emoji emoji);

    Optional<MemberPostReaction> findReactionByPostAndMemberIdAndEmoji(MemberPost post, String memberId, Emoji emoji);

    List<MemberPostReaction> findAllByPostId(String postId);
}
