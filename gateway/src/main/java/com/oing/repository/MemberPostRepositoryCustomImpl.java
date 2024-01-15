package com.oing.repository;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostDailyCalendarDTO;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.core.types.dsl.DateTimeTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.oing.domain.QMember.member;
import static com.oing.domain.QMemberPost.memberPost;

@RequiredArgsConstructor
public class MemberPostRepositoryCustomImpl implements MemberPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberPost> findLatestPostOfEveryday(List<String> memberIds, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory
                .selectFrom(memberPost)
                .where(memberPost.id.in(
                        JPAExpressions
                                .select(memberPost.id.max())
                                .from(memberPost)
                                .where(memberPost.memberId.in(memberIds)
                                        .and(memberPost.createdAt.between(startDate, endDate)))
                                .groupBy(Expressions.dateOperation(LocalDate.class, Ops.DateTimeOps.DATE, memberPost.createdAt))
                ))
                .orderBy(memberPost.createdAt.asc())
                .fetch();

    }


    @Override
    public List<MemberPostDailyCalendarDTO> findPostDailyCalendarDTOs(List<String> memberIds, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory
                .select(Projections.constructor(MemberPostDailyCalendarDTO.class, memberPost.id.count()))
                .from(memberPost)
                .where(memberPost.memberId.in(memberIds)
                        .and(memberPost.createdAt.between(startDate, endDate)))
                .groupBy(Expressions.dateOperation(LocalDate.class, Ops.DateTimeOps.DATE, memberPost.createdAt))
                .fetch();

    }

    @Override
    public QueryResults<MemberPost> searchPosts(int page, int size, LocalDate date, String memberId, String requesterMemberId, String familyId, boolean asc) {
        return queryFactory
                .select(memberPost)
                .from(memberPost)
                .leftJoin(member).on(memberPost.memberId.eq(member.id))
                .where(member.familyId.eq(familyId), eqDate(date), eqMemberId(memberId))
                .orderBy(asc ? memberPost.id.asc() : memberPost.id.desc())
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetchResults();
    }

    private BooleanExpression eqDate(LocalDate date) {
        DateTimeTemplate<LocalDate> createdAtDate = Expressions.dateTimeTemplate(LocalDate.class,
                "DATE({0})", memberPost.createdAt);

        return date == null ? null : createdAtDate.eq(date);
    }

    private BooleanExpression eqMemberId(String memberId) {
        return memberId == null ? null : memberPost.memberId.eq(memberId);
    }

    @Override
    public boolean existsByMemberIdAndCreatedAt(String memberId, LocalDate postDate) {
        DateTimeTemplate<LocalDate> createdAtDate = Expressions.dateTimeTemplate(LocalDate.class,
                "DATE({0})", memberPost.createdAt);

        return queryFactory
                .select(memberPost.id)
                .from(memberPost)
                .where(memberPost.memberId.eq(memberId)
                        .and(createdAtDate.eq(postDate)))
                .fetchFirst() != null;
    }
}
