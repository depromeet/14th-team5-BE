package com.oing.repository;

import com.oing.domain.MemberPostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberPostCommentRepository extends JpaRepository<MemberPostComment, String>, MemberPostCommentRepositoryCustom {
    void deleteAllByPostId(String memberPostId);
}
