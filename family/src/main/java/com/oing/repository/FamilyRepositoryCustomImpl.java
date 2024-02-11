package com.oing.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.oing.domain.QFamily.family;

@RequiredArgsConstructor
public class FamilyRepositoryCustomImpl implements FamilyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public int countByScoreDistinct() {
        return queryFactory
                .select(family.score)
                .from(family)
                .groupBy(family.score)
                .fetch()
                .size();
    }

    @Override
    public int countByScoreGreaterThanEqualScoreDistinct(int familyScore) {
        return queryFactory
                .select(family.score)
                .from(family)
                .where(family.score.goe(familyScore))
                .groupBy(family.score)
                .fetch()
                .size();
    }


}
