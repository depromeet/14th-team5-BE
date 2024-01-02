package com.oing.repository;

import com.oing.domain.model.MemberDevice;
import com.oing.domain.model.key.MemberDeviceKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDeviceRepository extends JpaRepository<MemberDevice, MemberDeviceKey> {
}
