package com.oing.repository;

import com.oing.domain.FamilyInviteLink;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 12/21/23
 * Time: 7:17 PM
 */
public interface FamilyInviteLinkRepository extends JpaRepository<FamilyInviteLink, String> {
    public FamilyInviteLink findByFamilyIdAndMemberId(String familyId, String memberId);
}
