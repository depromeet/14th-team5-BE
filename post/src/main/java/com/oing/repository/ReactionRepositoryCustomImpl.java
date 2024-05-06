package com.oing.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.oing.domain.QReaction.reaction;

@RequiredArgsConstructor
@Repository
public class ReactionRepositoryCustomImpl implements ReactionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public long countMonthlyReactionByMemberId(int year, int month, String memberId) {
        return queryFactory
                .select(reaction.count())
                .from(reaction)
                .where(reaction.memberId.eq(memberId),
                        reaction.createdAt.year().eq(year),
                        reaction.createdAt.month().eq(month))
                .fetchFirst();
    }
}
