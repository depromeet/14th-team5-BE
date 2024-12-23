package com.oing.service;

import com.oing.domain.UserNotificationHistory;
import com.oing.dto.request.CreateUserNotificationHistoryDTO;
import com.oing.repository.UserNotificationHistoryRepository;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserNotificationHistoryService {

    private final UserNotificationHistoryRepository userNotificationHistoryRepository;
    private final IdentityGenerator identityGenerator;

    @Transactional
    public UserNotificationHistory createUserNotificationHistory(CreateUserNotificationHistoryDTO createUserNotificationHistoryDTO) {
        return userNotificationHistoryRepository.save(
                createUserNotificationHistoryDTO.toEntity(identityGenerator.generateIdentity()));
    }

    public List<UserNotificationHistory> getRecentUserNotificationHistoriesByMemberId(String memberId) {
        // 최근 한 달간의 알림 내역 조회
        LocalDateTime oneMonthAgo = LocalDate.now().minusMonths(1).atStartOfDay();

        return userNotificationHistoryRepository.findByReceiverMemberIdAndCreatedAtAfter(memberId, oneMonthAgo);
    }
}
