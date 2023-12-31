package com.oing.repository;

import com.oing.domain.SocialMember;
import com.oing.domain.key.SocialMemberKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:45 AM
 */
public interface SocialMemberRepository extends JpaRepository<SocialMember, SocialMemberKey> {
    void deleteAllByMemberId(String memberId);
}
