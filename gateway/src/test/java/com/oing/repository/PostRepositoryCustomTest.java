package com.oing.repository;

import com.oing.config.QuerydslConfig;
import com.oing.domain.Family;
import com.oing.domain.Member;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.oing.domain.PostType.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // application-test.yaml의 데이터베이스 설정을 적용하기 위해서 필수
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class PostRepositoryCustomTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepositoryCustomImpl postRepositoryCustomImpl;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FamilyRepository familyRepository;


    private final Member testMember1 = new Member(
            "testMember1",
            "testFamily",
            LocalDate.of(1999, 10, 18),
            "testMember1",
            "profile.com/1",
            "1",
            LocalDateTime.now().minusDays(1)
    );

    private final Member testMember2 = new Member(
            "testMember2",
            "testFamily",
            LocalDate.of(1999, 10, 18),
            "testMember2",
            "profile.com/2",
            "2",
            LocalDateTime.now().minusDays(1)
    );

    private final Member testMember3 = new Member(
            "testMember3",
            "otherFamily",
            LocalDate.of(1999, 10, 18),
            "testMember3",
            "profile.com/3",
            "3",
            LocalDateTime.now().minusDays(1)
    );

    private final Member testMember4 = new Member(
            "testMember4",
            "testFamily",
            LocalDate.of(1999, 10, 18),
            "testMember4",
            "profile.com/4",
            "4",
            LocalDateTime.now()
    );


    @BeforeEach
    void setup() {
        // Family & Members
        familyRepository.save(new Family("testFamily"));
        memberRepository.save(testMember1);
        memberRepository.save(testMember2);
        memberRepository.save(testMember3);

        // Posts
        jdbcTemplate.execute("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('1', '" + testMember1.getId() + "', '" + testMember1.getFamilyId() + "', 1, 'SURVIVAL', 'https://storage.com/images/1', 0, 0, '2023-11-01 14:00:00', '2023-11-01 14:00:00', 'post1111', '1');");
        jdbcTemplate.execute("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('2', '" + testMember2.getId() + "', '" + testMember2.getFamilyId() + "', 1, 'SURVIVAL', 'https://storage.com/images/2', 0, 0, '2023-11-01 15:00:00', '2023-11-01 15:00:00', 'post2222', '2');");
        jdbcTemplate.execute("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('3', '" + testMember3.getId() + "', '" + testMember3.getFamilyId() + "', 1, 'SURVIVAL', 'https://storage.com/images/3', 0, 0, '2023-11-01 17:00:00', '2023-11-01 17:00:00', 'post3333', '3');");
        jdbcTemplate.execute("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('4', '" + testMember1.getId() + "', '" + testMember1.getFamilyId() + "', 1, 'SURVIVAL', 'https://storage.com/images/4', 0, 0, '2023-11-02 14:00:00', '2023-11-02 14:00:00', 'post4444', '4');");
    }


    @Test
    void 각_날짜에서_가장_마지막으로_업로드된_게시글을_조회한다() {
        // When
        String familyId = testMember1.getFamilyId();
        List<Post> posts = postRepositoryCustomImpl.findLatestPostOfEveryday(LocalDateTime.of(2023, 11, 1, 0, 0, 0), LocalDateTime.of(2023, 12, 1, 0, 0, 0), familyId);

        // Then
        assertThat(posts)
                .extracting(Post::getId)
                .containsExactly("2", "4");
    }

    @Nested
    class findLatestPost {
        @Test
        void 생존신고_게시물_정상_조회_테스트() {
            // given
            String familyId = testMember1.getFamilyId();
            LocalDateTime inclusiveStart = LocalDate.of(2023, 11, 1).atStartOfDay();
            LocalDateTime exclusiveEnd = LocalDate.of(2023, 11, 2).atStartOfDay();

            // when
            Post post = postRepositoryCustomImpl.findLatestPost(inclusiveStart, exclusiveEnd, SURVIVAL, familyId);

            // then
            assertThat(post.getId()).isEqualTo("2");
        }

        @Test
        void 시작날짜는_포함하고_종료날짜는_포함하지_않는다() {
            // given
            String familyId = testMember1.getFamilyId();
            LocalDateTime inclusiveStart = LocalDate.of(2023, 11, 1).atStartOfDay();
            LocalDateTime exclusiveEnd = LocalDate.of(2023, 11, 1).atStartOfDay();

            // when
            Post post = postRepositoryCustomImpl.findLatestPost(inclusiveStart, exclusiveEnd, SURVIVAL, familyId);

            // then
            assertThat(post).isNull();
        }

        @Test
        void 해당_날짜에_게시글이_없는_경우_null을_반환한다() {
            // given
            String familyId = testMember1.getFamilyId();
            LocalDateTime inclusiveStart = LocalDate.of(9999, 11, 3).atStartOfDay();
            LocalDateTime exclusiveEnd = LocalDate.of(9999, 11, 4).atStartOfDay();

            // when
            Post post = postRepositoryCustomImpl.findLatestPost(inclusiveStart, exclusiveEnd, SURVIVAL, familyId);

            // then
            assertThat(post).isNull();
        }
    }

    @Test
    void 특정_날짜에_게시글이_존재하는지_확인한다() {
        // given
        LocalDate postDate = LocalDate.of(2023, 11, 1);

        // when
        boolean exists = postRepositoryCustomImpl.existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(testMember1.getId(),
                testMember1.getFamilyId(), SURVIVAL, postDate);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 특정_날짜에_게시글이_존재하지_않는지_확인한다() {
        // given
        LocalDate postDate = LocalDate.of(2023, 11, 8);

        // when
        boolean exists = postRepositoryCustomImpl.existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(testMember1.getId(),
                testMember1.getFamilyId(), SURVIVAL, postDate);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void 해당_가족_구성원이_오늘_올린_생존신고_게시글_수를_조회한다() {
        // given
        String familyId = testMember1.getFamilyId();

        // when
        int todaySurvivalPostCount = postRepositoryCustomImpl.countTodaySurvivalPostsByFamilyId(familyId);

        // then
        assertThat(todaySurvivalPostCount).isEqualTo(0);
    }

    @Test
    void 어제_날짜를_기준으로_가족_구성원_수를_조회한다() {
        // given
        String familyId = testMember1.getFamilyId();

        // when
        int familyMemberCount = postRepositoryCustomImpl.countFamilyMembersByFamilyIdAtYesterday(familyId);

        // then
        assertThat(familyMemberCount).isEqualTo(2);
    }
}
