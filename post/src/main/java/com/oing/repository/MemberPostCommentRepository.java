package com.oing.repository;

import com.oing.domain.MemberPostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPostCommentRepository extends JpaRepository<MemberPostComment, String> {
}
