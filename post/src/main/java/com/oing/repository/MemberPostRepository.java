package com.oing.repository;

import com.oing.domain.MemberPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MemberPostRepository extends JpaRepository<MemberPost, String>, MemberPostRepositoryCustom {
    boolean existsByMemberIdAndCreatedAt(String memberId, LocalDate postDate);
}
