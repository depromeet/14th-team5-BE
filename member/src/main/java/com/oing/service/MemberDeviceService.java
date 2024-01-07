package com.oing.service;

import com.oing.domain.MemberDevice;
import com.oing.domain.key.MemberDeviceKey;
import com.oing.repository.MemberDeviceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2024/01/02
 * Time: 11:18 AM
 */
@RequiredArgsConstructor
@Service
public class MemberDeviceService {
    private final MemberDeviceRepository memberDeviceRepository;

    @Transactional
    public boolean addDevice(String memberId, String fcmToken) {
        if (memberDeviceRepository.existsById(new MemberDeviceKey(memberId, fcmToken))) {
            return false;
        }
        memberDeviceRepository.save(
                new MemberDevice(memberId, fcmToken)
        );
        return true;
    }

    @Transactional
    public boolean removeDevice(String memberId, String fcmToken) {
        if (!memberDeviceRepository.existsById(new MemberDeviceKey(memberId, fcmToken))) {
            return false;
        }
        memberDeviceRepository.deleteById(new MemberDeviceKey(memberId, fcmToken));
        return true;
    }
}
