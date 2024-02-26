package com.oing.repository;

import com.oing.domain.MemberPost;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.DateTimeTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
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
    public List<String> getMemberIdsPostedToday(LocalDate date) {
        return queryFactory
                .select(memberPost.memberId)
                .from(memberPost)
                .where(memberPost.createdAt.between(date.atStartOfDay(), date.atTime(23, 59, 59)))
                .fetch();
    }

    @Override
    public List<MemberPost> findLatestPostOfEveryday(LocalDateTime startDate, LocalDateTime endDate, String familyId) {
        return queryFactory
                .selectFrom(memberPost)
                .where(memberPost.id.in(
                        JPAExpressions
                                .select(memberPost.id.max())
                                .from(memberPost)
                                .where(memberPost.familyId.eq(familyId)
                                        .and(memberPost.createdAt.between(startDate, endDate)))
                                .groupBy(Expressions.dateOperation(LocalDate.class, Ops.DateTimeOps.DATE, memberPost.createdAt))
                ))
                .orderBy(memberPost.createdAt.asc())
                .fetch();

    }

    @Override
    public QueryResults<MemberPost> searchPosts(int page, int size, LocalDate date, String memberId, String requesterMemberId, String familyId, boolean asc) {
        return queryFactory
                .select(memberPost)
                .from(memberPost)
                .leftJoin(member).on(memberPost.memberId.eq(member.id))
                .where(memberPost.familyId.eq(familyId), eqDate(date), eqMemberId(memberId))
                .orderBy(asc ? memberPost.id.asc() : memberPost.id.desc())
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetchResults();
    }

    @Override
    public long countMonthlyPostByFamilyId(int year, int month, String familyId) {
        return queryFactory
                .select(memberPost.count())
                .from(memberPost)
                .where(memberPost.familyId.eq(familyId),
                        memberPost.createdAt.year().eq(year),
                        memberPost.createdAt.month().eq(month))
                .fetchFirst();
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
    public boolean existsByFamilyIdAndCreatedAt(String familyId, LocalDate postDate) {
        return queryFactory
                .select(memberPost.id)
                .from(memberPost)
                .where(
                        memberPost.familyId.eq(familyId),
                        dateExpr(memberPost.createdAt).eq(postDate)
                )
                .fetchFirst() != null;
    }

    @Override
    @Transactional
    public boolean existsByMemberIdAndFamilyIdAndCreatedAt(String memberId, String familyId, LocalDate postDate) {
        return queryFactory
                .select(memberPost.id)
                .from(memberPost)
                .where(
                        memberPost.memberId.eq(memberId),
                        memberPost.familyId.eq(familyId),
                        dateExpr(memberPost.createdAt).eq(postDate)
                )
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchFirst() != null;
    }

    private DateTimeTemplate<LocalDate> dateExpr(DateTimePath<LocalDateTime> localDateTime) {
        return Expressions.dateTimeTemplate(LocalDate.class, "DATE({0})", localDateTime);
    }
}
