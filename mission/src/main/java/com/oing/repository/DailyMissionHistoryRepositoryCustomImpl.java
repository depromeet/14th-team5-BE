package com.oing.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.oing.domain.QDailyMissionHistory.*;

@RequiredArgsConstructor
public class DailyMissionHistoryRepositoryCustomImpl implements DailyMissionHistoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findRecentDailyMissionIdsOrderByDateAsc(long count) {
        return queryFactory
                .selectDistinct(dailyMissionHistory.mission.id)
                .from(dailyMissionHistory)
                .orderBy(dailyMissionHistory.date.asc())
                .limit(count)
                .fetch();
    }
}
