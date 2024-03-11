package com.oing.repository;

import com.oing.domain.Family;
import com.oing.domain.QFamily;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

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

    @Override
    public Optional<Family> findByIdWithLock(String familyId) {
        Family family = queryFactory
                .selectFrom(QFamily.family)
                .where(QFamily.family.id.eq(familyId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchFirst();

        return Optional.ofNullable(family);
    }
}
