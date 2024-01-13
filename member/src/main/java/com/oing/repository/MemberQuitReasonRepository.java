package com.oing.repository;

import com.oing.domain.MemberQuitReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberQuitReasonRepository extends JpaRepository<MemberQuitReason, String> {
}
