package com.oing.repository;

import com.oing.domain.MemberDevice;
import com.oing.domain.key.MemberDeviceKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDeviceRepository extends JpaRepository<MemberDevice, MemberDeviceKey> {
}
