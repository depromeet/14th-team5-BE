package com.oing.repository;

import com.oing.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:52 AM
 */
public interface MemberRepository extends JpaRepository<Member, String> {
    List<Member> findAllByFamilyId(String familyId);

    Page<Member> findAllByFamilyIdAndDeletedAtIsNull(String familyId, PageRequest pageRequest);

    long countByFamilyIdAndFamilyJoinedAtBefore(String familyId, LocalDate localDate);
}
