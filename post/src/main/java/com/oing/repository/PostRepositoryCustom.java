package com.oing.repository;

import com.oing.domain.Post;
import com.oing.domain.Type;
import com.querydsl.core.QueryResults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PostRepositoryCustom {
    List<String> getMemberIdsPostedToday(LocalDate date);

    List<Post> findLatestPostOfEveryday(LocalDateTime startDate, LocalDateTime endDate, String familyId);

    QueryResults<Post> searchPosts(int page, int size, LocalDate date, String memberId, String requesterMemberId,
                                   String familyId, boolean asc, Type type);

    long countMonthlyPostByFamilyId(int year, int month, String familyId);

    boolean existsByFamilyIdAndCreatedAt(String familyId, LocalDate postDate);

    boolean existsByMemberIdAndFamilyIdAndCreatedAt(String memberId, String familyId, LocalDate postDate);

}
