package com.oing.repository;

import com.oing.domain.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 1/4/24
 * Time: 11:51 AM
 */
public interface AppVersionRepository extends JpaRepository<AppVersion, UUID> {
}
