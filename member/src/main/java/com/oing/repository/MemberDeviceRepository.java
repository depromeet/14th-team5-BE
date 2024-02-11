package com.oing.repository;

import com.oing.domain.MemberDevice;
import com.oing.domain.key.MemberDeviceKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberDeviceRepository extends JpaRepository<MemberDevice, MemberDeviceKey> {
    List<MemberDevice> findAllByMemberId(String memberId);
    void deleteAllByFcmToken(String fcmToken);
    void deleteAllByMemberId(String fcmToken);
}
