package com.oing.repository;

import com.oing.config.QuerydslConfig;
import com.oing.domain.Family;
import com.oing.domain.Member;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // application-test.yaml의 데이터베이스 설정을 적용하기 위해서 필수
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
public class MemberRepositoryCustomTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MemberRepositoryCustomImpl memberRepositoryCustomImpl;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FamilyRepository familyRepository;

    private final Member testMember1 = new Member(
            "testMember1",
            "testFamily",
            LocalDate.of(1999, 10, 18),
            "testMember1",
            null,
            null,
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


    @BeforeEach
    void setup() {
        // Family & Members
        familyRepository.save(new Family("testFamily", null));
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
    void 프로필_이미지가_존재하는_회원_닉네임부터_조회한다() {
        // When
        String familyId = testMember1.getFamilyId();
        List<String> memberNames = memberRepositoryCustomImpl.findFamilyMemberNamesByFamilyId(familyId);

        // Then
        assertThat(memberNames).containsExactly("testMember2", "testMember1");
    }

    @Test
    void 프로필_이미지가_존재하는_회원_이미지부터_조회한다() {
        // When
        String familyId = testMember1.getFamilyId();
        List<String> memberProfileImgUrls = memberRepositoryCustomImpl.findFamilyMemberProfileImgUrlsByFamilyId(familyId);

        // Then
        assertThat(memberProfileImgUrls).containsExactly("profile.com/2", null);
    }
}
