package com.oing.repository;

import com.oing.domain.UserNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserNotificationHistoryRepository extends JpaRepository<UserNotificationHistory, String> {
    List<UserNotificationHistory> findByReceiverMemberIdAndCreatedAtAfter(String receiverMemberId, LocalDateTime createdAt);
}
