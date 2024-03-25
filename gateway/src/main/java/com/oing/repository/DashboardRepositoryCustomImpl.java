package com.oing.repository;

import com.oing.domain.DateCountProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.oing.domain.QMember.member;
import static com.oing.domain.QPost.post;
import static com.oing.domain.QComment.comment;
import static com.oing.domain.QReaction.reaction;
import static com.oing.domain.QRealEmoji.realEmoji1;

@Repository
@RequiredArgsConstructor
public class DashboardRepositoryCustomImpl {
    private final JPAQueryFactory queryFactory;

    public long getTotalMemberCount(LocalDate untilDate) {
        LocalDateTime untilTime = untilDate.plusDays(1L).atStartOfDay(); //untilDate + 1의 00:00:00까지 = untilDate까지 가입자
        return queryFactory
                .select(member.count())
                .from(member)
                .where(member.createdAt.lt(untilTime))
                .fetchFirst();
    }

    public long getTotalFamilyCount(LocalDate untilDate) {
        LocalDateTime untilTime = untilDate.plusDays(1L).atStartOfDay();
        return queryFactory
                .select(member.familyId)
                .from(member)
                .groupBy(member.familyId)
                .where(member.createdAt.lt(untilTime))
                .fetch()
                .size();
    }

    public long getTotalPostCount(LocalDate untilDate) {
        LocalDateTime untilTime = untilDate.plusDays(1L).atStartOfDay();
        return queryFactory
                .select(post.count())
                .from(post)
                .where(post.createdAt.lt(untilTime))
                .fetchFirst();
    }

    public long getTotalPostCommentCount(LocalDate untilDate) {
        LocalDateTime untilTime = untilDate.plusDays(1L).atStartOfDay();
        return queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.createdAt.lt(untilTime))
                .fetchFirst();
    }

    public long getTotalPostReactionCount(LocalDate untilDate) {
        LocalDateTime untilTime = untilDate.plusDays(1L).atStartOfDay();
        long emojiCnt = queryFactory
                .select(reaction.count())
                .from(reaction)
                .where(reaction.createdAt.lt(untilTime))
                .fetchFirst();
        long realEmojiCnt = queryFactory
                .select(realEmoji1.count())
                .from(realEmoji1)
                .where(realEmoji1.createdAt.lt(untilTime))
                .fetchFirst();
        return emojiCnt + realEmojiCnt;
    }

    public List<Long> getFamilyMemberDistribution() {
        return queryFactory
                .select(member.count())
                .from(member)
                .groupBy(member.familyId)
                .fetch();
    }

    public List<DateCountProjection> getNewMemberCount(LocalDate startDate, LocalDate endDate) {
        DateTemplate<LocalDate> createdAtDate =
                Expressions.dateTemplate(LocalDate.class, "DATE({0})", member.createdAt);
        return queryFactory
                .select(Projections.constructor(DateCountProjection.class, createdAtDate, member.count()))
                .from(member)
                .where(createdAtDate.goe(startDate), createdAtDate.loe(endDate))
                .groupBy(createdAtDate)
                .fetch();
    }

    public List<DateCountProjection> getNewPostCount(LocalDate startDate, LocalDate endDate) {
        DateTemplate<LocalDate> createdAtDate =
                Expressions.dateTemplate(LocalDate.class, "DATE({0})", post.createdAt);
        return queryFactory
                .select(Projections.constructor(DateCountProjection.class, createdAtDate, post.count()))
                .from(post)
                .where(createdAtDate.goe(startDate), createdAtDate.loe(endDate))
                .groupBy(createdAtDate)
                .fetch();
    }
}
