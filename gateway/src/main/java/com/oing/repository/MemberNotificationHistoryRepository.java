package com.oing.repository;

import com.oing.domain.MemberNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberNotificationHistoryRepository extends JpaRepository<MemberNotificationHistory, String> {
    List<MemberNotificationHistory> findByReceiverMemberIdAndCreatedAtAfter(String receiverMemberId, LocalDateTime createdAt);
}
