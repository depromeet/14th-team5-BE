package com.oing.repository;

import com.oing.domain.MemberPostCountDTO;
import com.oing.domain.PaginationDTO;
import com.oing.domain.model.MemberPost;
import com.oing.domain.model.QMemberPost;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.oing.domain.model.QMemberPost.*;

@RequiredArgsConstructor
public class MemberPostRepositoryImpl implements MemberPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<MemberPost> findLatestPostOfEveryday(List<String> memberIds, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory
                .selectFrom(memberPost)
                .where(memberPost.memberId.in(memberIds)
                        .and(memberPost.createdAt.between(startDate, endDate)))
                .groupBy(memberPost.createdAt)
                .having(memberPost.createdAt.eq(memberPost.createdAt.max()))
                .orderBy(memberPost.createdAt.asc())
                .fetch();
    }

    @Override
    public List<MemberPostCountDTO> countPostsOfEveryday(List<String> memberIds, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory
                .select(Projections.bean(MemberPostCountDTO.class, memberPost.createdAt, memberPost.count()))
                .from(memberPost)
                .where(memberPost.memberId.in(memberIds)
                        .and(memberPost.createdAt.between(startDate, endDate)))
                .groupBy(memberPost.createdAt)
                .orderBy(memberPost.createdAt.asc())
                .fetch();
    }

    @Override
    public QueryResults<MemberPost> searchPosts(int page, int size, LocalDate date, String memberId, boolean asc) {
        JPAQueryBase<MemberPost, ?> baseQuery = queryFactory
                .select(memberPost)
                .from(memberPost);

        if(date != null) baseQuery = baseQuery.where(memberPost.postDate.eq(date));
        if(memberId != null) baseQuery = baseQuery.where(memberPost.memberId.eq(memberId));

        return baseQuery
                .orderBy(asc ? memberPost.id.asc() : memberPost.id.desc())
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetchResults();
    }
}
