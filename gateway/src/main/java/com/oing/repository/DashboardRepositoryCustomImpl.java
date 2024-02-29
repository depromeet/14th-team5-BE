package com.oing.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.oing.domain.QMember.member;
import static com.oing.domain.QMemberPost.memberPost;
import static com.oing.domain.QMemberPostComment.memberPostComment;
import static com.oing.domain.QMemberPostReaction.memberPostReaction;
import static com.oing.domain.QMemberPostRealEmoji.memberPostRealEmoji;

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
                .select(memberPost.count())
                .from(memberPost)
                .where(memberPost.createdAt.lt(untilTime))
                .fetchFirst();
    }

    public long getTotalPostCommentCount(LocalDate untilDate) {
        LocalDateTime untilTime = untilDate.plusDays(1L).atStartOfDay();
        return queryFactory
                .select(memberPostComment.count())
                .from(memberPostComment)
                .where(memberPostComment.createdAt.lt(untilTime))
                .fetchFirst();
    }

    public long getTotalPostReactionCount(LocalDate untilDate) {
        LocalDateTime untilTime = untilDate.plusDays(1L).atStartOfDay();
        long emojiCnt = queryFactory
                .select(memberPostReaction.count())
                .from(memberPostReaction)
                .where(memberPostReaction.createdAt.lt(untilTime))
                .fetchFirst();
        long realEmojiCnt = queryFactory
                .select(memberPostRealEmoji.count())
                .from(memberPostRealEmoji)
                .where(memberPostRealEmoji.createdAt.lt(untilTime))
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
}
