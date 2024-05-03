package com.oing.job;

import com.oing.domain.DailyMissionHistory;
import com.oing.domain.Mission;
import com.oing.repository.DailyMissionHistoryRepository;
import com.oing.repository.MissionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DailyMissionJobTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DailyMissionJob dailyMissionJob;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private DailyMissionHistoryRepository dailyMissionHistoryRepository;


    private Mission testMission1;
    private Mission testMission2;
    private Mission testMission3;
    private Mission testMission4;
    private Mission testMission5;
    private Mission testMission6;
    private Mission testMission7;


    @BeforeEach
    void setUp() {
        testMission1 = missionRepository.save(Mission.builder()
                .id("testMission1")
                .content("testMission1")
                .build());

        testMission2 = missionRepository.save(Mission.builder()
                .id("testMission2")
                .content("testMission2")
                .build());

        testMission3 = missionRepository.save(Mission.builder()
                .id("testMission3")
                .content("testMission3")
                .build());

        testMission4 = missionRepository.save(Mission.builder()
                .id("testMission4")
                .content("testMission4")
                .build());

        testMission5 = missionRepository.save(Mission.builder()
                .id("testMission5")
                .content("testMission5")
                .build());

        testMission6 = missionRepository.save(Mission.builder()
                .id("testMission6")
                .content("testMission6")
                .build());

        testMission7 = missionRepository.save(Mission.builder()
                .id("testMission7")
                .content("testMission7")
                .build());

        // Table Lock 용 테이블 생성
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS shedlock\n" +
                "(\n" +
                "    name       VARCHAR(64)  NOT NULL,\n" +
                "    lock_until TIMESTAMP(3) NOT NULL,\n" +
                "    locked_at  TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),\n" +
                "    locked_by  VARCHAR(255) NOT NULL,\n" +
                "    PRIMARY KEY (name)\n" +
                ") DEFAULT CHARSET = utf8mb4\n" +
                "  COLLATE = utf8mb4_unicode_ci COMMENT 'ShedLock 테이블';");
        // Table Lock 비활성화
        jdbcTemplate.update("UPDATE shedlock SET lock_until = locked_at WHERE name = 'DailyMissionRegistrationJob'");
    }


    @Nested
    class registerDailyMission {
        @Test
        void 정상_등록_테스트() {
            // given
            dailyMissionHistoryRepository.save(DailyMissionHistory.builder()
                    .date(LocalDate.now().minusDays(1))
                    .mission(testMission1)
                    .build());
            dailyMissionHistoryRepository.save(DailyMissionHistory.builder()
                    .date(LocalDate.now().minusDays(2))
                    .mission(testMission2)
                    .build());
            dailyMissionHistoryRepository.save(DailyMissionHistory.builder()
                    .date(LocalDate.now().minusDays(3))
                    .mission(testMission3)
                    .build());

            // when
            dailyMissionJob.registerDailyMission();

            // then
            Optional<DailyMissionHistory> dailyMissionHistory = dailyMissionHistoryRepository.findById(LocalDate.now());
            assertThat(dailyMissionHistory).isPresent();
            assertThat(dailyMissionHistory.get().getMission().getId())
                    .isNotIn(testMission1.getId(), testMission2.getId(), testMission3.getId())
                    .isIn(testMission4.getId(), testMission5.getId(), testMission6.getId(), testMission7.getId());
        }

        @Test
        void 제외된_미션_외_미션이_없는경우_가장_오래된_미션을_선출한다() {
            // given
            dailyMissionHistoryRepository.save(DailyMissionHistory.builder()
                    .date(LocalDate.now().minusDays(1))
                    .mission(testMission1)
                    .build());
            dailyMissionHistoryRepository.save(DailyMissionHistory.builder()
                    .date(LocalDate.now().minusDays(2))
                    .mission(testMission2)
                    .build());
            dailyMissionHistoryRepository.save(DailyMissionHistory.builder()
                    .date(LocalDate.now().minusDays(3))
                    .mission(testMission3)
                    .build());
            dailyMissionHistoryRepository.save(DailyMissionHistory.builder()
                    .date(LocalDate.now().minusDays(4))
                    .mission(testMission4)
                    .build());
            dailyMissionHistoryRepository.save(DailyMissionHistory.builder()
                    .date(LocalDate.now().minusDays(5))
                    .mission(testMission5)
                    .build());
            dailyMissionHistoryRepository.save(DailyMissionHistory.builder()
                    .date(LocalDate.now().minusDays(6))
                    .mission(testMission6)
                    .build());
            dailyMissionHistoryRepository.save(DailyMissionHistory.builder()
                    .date(LocalDate.now().minusDays(7))
                    .mission(testMission7)
                    .build());

            // when
            dailyMissionJob.registerDailyMission();

            // then
            Optional<DailyMissionHistory> dailyMissionHistory = dailyMissionHistoryRepository.findById(LocalDate.now());
            assertThat(dailyMissionHistory).isPresent();
            assertThat(dailyMissionHistory.get().getMission().getId()).isEqualTo(testMission7.getId());
        }

        @Test
        void 등록된_미션이_없는경우_예외핸들링을_한다() {
            // when
            assertThatNoException().isThrownBy(() -> dailyMissionJob.registerDailyMission());
        }
    }
}
