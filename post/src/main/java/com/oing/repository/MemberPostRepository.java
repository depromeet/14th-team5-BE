package com.oing.repository;

import com.oing.domain.MemberPost;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberPostRepository extends JpaRepository<MemberPost, String>, MemberPostRepositoryCustom {

    long countByMemberIdInAndCreatedAtBetween(List<String> memberIds, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
