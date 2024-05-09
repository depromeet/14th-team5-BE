package com.oing.repository;

import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.querydsl.core.BooleanBuilder;
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
import java.time.ZonedDateTime;
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
    public Post findLatestPost(LocalDateTime startDate, LocalDateTime endDate, PostType postType, String familyId) {
        return queryFactory
                .selectFrom(post)
                .where(
                        post.type.eq(postType),
                        post.familyId.eq(familyId),
                        post.createdAt.between(startDate, endDate)
                )
                .orderBy(post.createdAt.desc())
                .fetchFirst();
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

    @Override
    public long countMonthlyPostByMemberId(int year, int month, String memberId) {
        return queryFactory
                .select(post.count())
                .from(post)
                .where(post.memberId.eq(memberId),
                        post.createdAt.year().eq(year),
                        post.createdAt.month().eq(month))
                .fetchFirst();
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
    public boolean existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(String memberId, String familyId, PostType type, LocalDate postDate) {
        return queryFactory
                .select(post.id)
                .from(post)
                .where(
                        post.memberId.eq(memberId),
                        post.familyId.eq(familyId),
                        post.type.eq(type),
                        dateExpr(post.createdAt).eq(postDate)
                )
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchFirst() != null;
    }

    @Override
    public int countFamilyMembersByFamilyIdAtYesterday(String familyId) {
        LocalDate today = ZonedDateTime.now().toLocalDate();
        Long count = queryFactory
                .select(member.id.count())
                .from(member)
                .where(member.familyId.eq(familyId)
                        .and(dateExpr(member.familyJoinAt).before(today))
                        .and(isActiveMember()))
                .fetchFirst();
        return count.intValue();
    }

    @Override
    public int countTodaySurvivalPostsByFamilyId(String familyId) {
        LocalDate today = ZonedDateTime.now().toLocalDate();
        Long count = queryFactory
                .select(post.id.count())
                .from(post)
                .where(post.familyId.eq(familyId),
                        post.type.eq(PostType.SURVIVAL),
                        dateExpr(post.createdAt).eq(today))
                .fetchFirst();
        return count.intValue();
    }

    private BooleanExpression eqDate(LocalDate date) {
        DateTimeTemplate<LocalDate> createdAtDate = Expressions.dateTimeTemplate(LocalDate.class,
                "DATE({0})", post.createdAt);

        return date == null ? null : createdAtDate.eq(date);
    }

    private BooleanBuilder eqMonth(LocalDate date) {
        return date == null ? null : new BooleanBuilder()
                .and(post.createdAt.year().eq(date.getYear()))
                .and(post.createdAt.month().eq(date.getMonthValue()));
    }

    private DateTimeTemplate<LocalDate> dateExpr(DateTimePath<LocalDateTime> localDateTime) {
        return Expressions.dateTimeTemplate(LocalDate.class, "DATE({0})", localDateTime);
    }

    private BooleanExpression eqMemberId(String memberId) {
        return memberId == null ? null : post.memberId.eq(memberId);
    }

    private BooleanExpression isActiveMember() {
        return member.deletedAt.isNull();
    }

    private BooleanExpression eqPostType(PostType postType) {
        return postType == null ? null : post.type.eq(postType);
    }
}
