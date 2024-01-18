package com.oing.repository;

import com.oing.domain.MemberQuitReason;
import com.oing.domain.key.MemberQuitReasonKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberQuitReasonRepository extends JpaRepository<MemberQuitReason, MemberQuitReasonKey> {
}
