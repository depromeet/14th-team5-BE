package com.oing.repository;

import java.util.List;

public interface DailyMissionHistoryRepositoryCustom {

    List<String> findRecentDailyMissionIdsOrderByDateAsc(long count);
}
