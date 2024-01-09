package com.oing.repository;

import com.oing.domain.DeepLink;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 1/3/24
 * Time: 10:20 AM
 */
public interface DeepLinkRepository extends JpaRepository<DeepLink, String> {
}
