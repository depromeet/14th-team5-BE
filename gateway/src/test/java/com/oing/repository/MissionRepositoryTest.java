package com.oing.repository;

import com.oing.config.QuerydslConfig;
import com.oing.domain.Mission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // application-test.yaml의 데이터베이스 설정을 적용하기 위해서 필수
@Import(QuerydslConfig.class)
public class MissionRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;

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
    }


    @Nested
    class getRandomMissionExcludingIds {

        @Test
        void 정상_조회_테스트() {
            // given
            List<String> excludingIds = List.of(testMission1.getId(), testMission2.getId(), testMission3.getId());

            // when
            Optional<Mission> randomMissionId = missionRepository.findRandomMissionExcludingIds(excludingIds);

            // then
            assertThat(randomMissionId).isPresent();
            assertThat(randomMissionId.get().getId())
                    .isNotIn(excludingIds)
                    .isIn(testMission4.getId(), testMission5.getId(), testMission6.getId(), testMission7.getId());
        }

        @Test
        void 아이디_파라미터_제외_테스트() {
            // given
            List<String> excludingIds = List.of(testMission1.getId(), testMission2.getId(), testMission3.getId(), testMission4.getId(), testMission5.getId(), testMission6.getId(), testMission7.getId());

            // when
            Optional<Mission> randomMissionId = missionRepository.findRandomMissionExcludingIds(excludingIds);

            // then
            assertThat(randomMissionId).isNotPresent();
        }
    }
}
