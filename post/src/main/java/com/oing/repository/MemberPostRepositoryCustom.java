package com.oing.repository;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostDailyCalendarDTO;
import com.querydsl.core.QueryResults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MemberPostRepositoryCustom {

    List<MemberPost> findLatestPostOfEveryday(List<String> memberIds, LocalDateTime startDate, LocalDateTime endDate);

    List<MemberPostDailyCalendarDTO> findPostDailyCalendarDTOs(List<String> memberIds, LocalDateTime startDate, LocalDateTime endDate);

    QueryResults<MemberPost> searchPosts(int page, int size, LocalDate date, String memberId, String requesterMemberId, String familyId, boolean asc);

    boolean existsByMemberIdAndCreatedAt(String memberId, LocalDate postDate);
}
