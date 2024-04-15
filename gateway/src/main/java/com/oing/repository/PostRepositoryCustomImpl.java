package com.oing.repository;

import com.oing.domain.Post;
import com.oing.domain.PostType;
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
import static com.oing.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> getMemberIdsPostedToday(LocalDate date) {
        return queryFactory
                .select(post.memberId)
                .from(post)
                .where(post.createdAt.between(date.atStartOfDay(), date.atTime(23, 59, 59)))
                .fetch();
    }

    @Override
    public List<Post> findLatestPostOfEveryday(LocalDateTime startDate, LocalDateTime endDate, String familyId) {
        return queryFactory
                .selectFrom(post)
                .where(post.id.in(
                        JPAExpressions
                                .select(post.id.max())
                                .from(post)
                                .where(post.familyId.eq(familyId)
                                        .and(post.createdAt.between(startDate, endDate)))
                                .groupBy(Expressions.dateOperation(LocalDate.class, Ops.DateTimeOps.DATE, post.createdAt))
                ))
                .orderBy(post.createdAt.asc())
                .fetch();

    }

    @Override
    public QueryResults<Post> searchPosts(int page, int size, LocalDate date, String memberId, String requesterMemberId,
                                          String familyId, boolean asc, PostType type) {
        return queryFactory
                .select(post)
                .from(post)
                .leftJoin(member).on(post.memberId.eq(member.id))
                .where(post.familyId.eq(familyId), eqDate(date), eqMemberId(memberId), post.type.eq(type))
                .orderBy(asc ? post.id.asc() : post.id.desc())
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetchResults();
    }

    @Override
    public long countMonthlyPostByFamilyId(int year, int month, String familyId) {
        return queryFactory
                .select(post.count())
                .from(post)
                .where(post.familyId.eq(familyId),
                        post.createdAt.year().eq(year),
                        post.createdAt.month().eq(month))
                .fetchFirst();
    }

    private BooleanExpression eqDate(LocalDate date) {
        DateTimeTemplate<LocalDate> createdAtDate = Expressions.dateTimeTemplate(LocalDate.class,
                "DATE({0})", post.createdAt);

        return date == null ? null : createdAtDate.eq(date);
    }

    private BooleanExpression eqMemberId(String memberId) {
        return memberId == null ? null : post.memberId.eq(memberId);
    }

    @Override
    public boolean existsByFamilyIdAndCreatedAt(String familyId, LocalDate postDate) {
        return queryFactory
                .select(post.id)
                .from(post)
                .where(
                        post.familyId.eq(familyId),
                        dateExpr(post.createdAt).eq(postDate)
                )
                .fetchFirst() != null;
    }

    @Override
    @Transactional
    public boolean existsByMemberIdAndFamilyIdAndCreatedAt(String memberId, String familyId, LocalDate postDate) {
        return queryFactory
                .select(post.id)
                .from(post)
                .where(
                        post.memberId.eq(memberId),
                        post.familyId.eq(familyId),
                        dateExpr(post.createdAt).eq(postDate)
                )
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchFirst() != null;
    }

    private DateTimeTemplate<LocalDate> dateExpr(DateTimePath<LocalDateTime> localDateTime) {
        return Expressions.dateTimeTemplate(LocalDate.class, "DATE({0})", localDateTime);
    }
}
