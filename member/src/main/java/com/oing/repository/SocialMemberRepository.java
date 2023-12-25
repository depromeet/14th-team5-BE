package com.oing.repository;

import com.oing.domain.model.Member;
import com.oing.domain.model.SocialMember;
import com.oing.domain.model.key.SocialMemberKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:45 AM
 */
public interface SocialMemberRepository extends JpaRepository<SocialMember, SocialMemberKey> {
    List<SocialMember> findAllSocialMemberByMember(Member member);
}
