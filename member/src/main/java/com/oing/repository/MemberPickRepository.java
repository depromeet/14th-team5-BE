package com.oing.repository;

import com.oing.domain.MemberPick;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/2/24
 * Time: 11:54 AM
 */
public interface MemberPickRepository extends JpaRepository<MemberPick, String> {
    MemberPick findByFamilyIdAndFromMemberIdAndDateAndToMemberId(String familyId, String fromMemberId, LocalDate date, String toMemberId);
    List<MemberPick> findAllByFamilyIdAndDateAndToMemberId(String familyId, LocalDate date, String toMemberId);
    List<MemberPick> findAllByFamilyIdAndDateAndFromMemberId(String familyId, LocalDate date, String fromMemberId);
}
