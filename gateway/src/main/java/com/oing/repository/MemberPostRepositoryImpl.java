package com.oing.repository;

import com.oing.domain.MemberPostCountDTO;
import com.oing.domain.model.Member;
import com.oing.domain.model.MemberPost;
import com.oing.exception.FamilyNotFoundException;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.oing.domain.model.QMemberPost.*;
import static com.oing.domain.model.QMember.*;

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
                .select(Projections.fields(MemberPostCountDTO.class, memberPost.createdAt, memberPost.count().as("count")))
                .from(memberPost)
                .where(memberPost.memberId.in(memberIds)
                        .and(memberPost.createdAt.between(startDate, endDate)))
                .groupBy(memberPost.createdAt)
                .orderBy(memberPost.createdAt.asc())
                .fetch();
    }

    @Override
    public QueryResults<MemberPost> searchPosts(int page, int size, LocalDate date, String memberId, String requesterMemberId, boolean asc) {
        String requesterFamilyId = getFamilyIdOfMember(requesterMemberId);
        return queryFactory
                .select(memberPost)
                .from(memberPost)
                .leftJoin(member).on(memberPost.memberId.eq(member.id))
                .where(member.familyId.eq(requesterFamilyId), eqDate(date), eqMemberId(memberId))
                .orderBy(asc ? memberPost.id.asc() : memberPost.id.desc())
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetchResults();
    }

    private String getFamilyIdOfMember(String memberId) {
        String requesterFamilyId = queryFactory
                .select(member)
                .from(member)
                .where(member.id.eq(memberId))
                .fetchFirst()
                .getFamilyId();
        if (requesterFamilyId == null) throw new FamilyNotFoundException();
        return requesterFamilyId;
    }

    private BooleanExpression eqDate(LocalDate date) {
        return date == null ? null : memberPost.postDate.eq(date);
    }

    private BooleanExpression eqMemberId(String memberId) {
        return memberId == null ? null : memberPost.memberId.eq(memberId);
    }
}
