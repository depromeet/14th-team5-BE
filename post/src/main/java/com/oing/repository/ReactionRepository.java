package com.oing.repository;

import com.oing.domain.Emoji;
import com.oing.domain.Post;
import com.oing.domain.Reaction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, String>, ReactionRepositoryCustom {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByPostAndMemberIdAndEmoji(Post post, String memberId, Emoji emoji);

    Optional<Reaction> findReactionByPostAndMemberIdAndEmoji(Post post, String memberId, Emoji emoji);

    List<Reaction> findAllByPostId(String postId);

    void deleteAllByPostId(String memberPostId);
}
