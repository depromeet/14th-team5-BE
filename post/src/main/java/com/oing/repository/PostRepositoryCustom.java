package com.oing.repository;

import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.querydsl.core.QueryResults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PostRepositoryCustom {
    List<String> getMemberIdsPostedToday(LocalDate date);

    List<Post> findLatestPostOfEveryday(LocalDateTime startDate, LocalDateTime endDate, String familyId);

    Post findLatestPost(LocalDateTime startDate, LocalDateTime endDate, String familyId);

    QueryResults<Post> searchPosts(int page, int size, LocalDate date, String memberId, String requesterMemberId,
                                   String familyId, boolean asc, PostType type);

    long countMonthlyPostByFamilyId(int year, int month, String familyId);

    long countMonthlyPostByMemberId(int year, int month, String memberId);

    boolean existsByFamilyIdAndCreatedAt(String familyId, LocalDate postDate);

    boolean existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(String memberId, String familyId, PostType type, LocalDate postDate);

    int countFamilyMembersByFamilyIdAtYesterday(String familyId);

    int countTodaySurvivalPostsByFamilyId(String familyId);
}
