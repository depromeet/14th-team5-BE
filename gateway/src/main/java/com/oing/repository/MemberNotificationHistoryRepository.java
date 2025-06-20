package com.oing.repository;

import com.oing.domain.MemberNotificationHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberNotificationHistoryRepository extends JpaRepository<MemberNotificationHistory, String> {
    Page<MemberNotificationHistory> findByReceiverMemberIdAndCreatedAtAfterOrderByCreatedAtDesc(
            String receiverMemberId, LocalDateTime createdAt, Pageable pageable);

    @Query("SELECT m.id FROM member_notification_history m WHERE m.receiverMemberId = :receiverMemberId ORDER BY m.createdAt DESC LIMIT 1")
    Optional<String> findLatestNotificationIdByReceiverMemberId(String receiverMemberId);
}
