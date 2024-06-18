package com.oing.repository;

import com.oing.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:52 AM
 */
public interface MemberRepository extends JpaRepository<Member, String>, MemberRepositoryCustom {
    List<Member> findAllByFamilyIdAndDeletedAtIsNull(String familyId);

    Page<Member> findAllByFamilyIdAndDeletedAtIsNull(String familyId, PageRequest pageRequest);

    List<Member> findAllByFamilyIdAndFamilyJoinAtBeforeAndDeletedAtIsNull(String familyId, LocalDateTime dateTime);

    List<Member> findAllByDeletedAtIsNull();

    boolean existsByIdAndDeletedAtNotNull(String memberId);

    int countByFamilyIdAndDeletedAtIsNull(String familyId);
}
