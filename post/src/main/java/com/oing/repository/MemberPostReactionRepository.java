package com.oing.repository;

import com.oing.domain.model.MemberPostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPostReactionRepository extends JpaRepository<MemberPostReaction, String> {
}
