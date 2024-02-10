package com.oing.repository;

import com.oing.domain.MemberPost;
import com.querydsl.core.QueryResults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MemberPostRepositoryCustom {
    List<String> getMemberIdsPostedToday(LocalDate date);

    List<MemberPost> findLatestPostOfEveryday(LocalDateTime startDate, LocalDateTime endDate, String familyId);

    QueryResults<MemberPost> searchPosts(int page, int size, LocalDate date, String memberId, String requesterMemberId, String familyId, boolean asc);

    long countMonthlyPostByFamilyId(int year, int month, String familyId);

    boolean existsByMemberIdAndFamilyIdAndCreatedAt(String memberId, String familyId, LocalDate postDate);
}
