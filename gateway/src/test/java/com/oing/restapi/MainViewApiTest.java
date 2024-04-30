package com.oing.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.domain.*;
import com.oing.repository.CommentRepository;
import com.oing.repository.MemberRepository;
import com.oing.repository.PostRepository;
import com.oing.repository.ReactionRepository;
import com.oing.service.TokenGenerator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MainViewApiTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private ObjectMapper objectMapper;

    private String TEST_MEMBER1_ID = "01HGW2N7EHJVJ4CJ999RRS2E91";
    private Member TEST_MEMBER1;
    private String TEST_MEMBER2_ID = "01HGW2N7EHJVJ4CJ999RRS2E99";
    private Member TEST_MEMBER2;
    private String TEST_MEMBER3_ID = "99999999999999999999999999";
    private String TEST_POST_ID = "01HGW2N7EHJVJ4CJ999RRS2A97";
    private String TEST_FAMILY_ID = "01HGW2N7EHJVJ4CJ999RRS2E44";
    private String TEST_MEMBER1_TOKEN;
    private String TEST_MEMBER2_TOKEN;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReactionRepository reactionRepository;

    @BeforeEach
    void setUp() {
        TEST_MEMBER1 = memberRepository.save(
                new Member(
                        TEST_MEMBER1_ID,
                        TEST_FAMILY_ID,
                        LocalDate.now(),
                        "member1", "https://profile.co.kr", "profile.co.kr",
                        LocalDateTime.now()
                )
        );
        TEST_MEMBER1_TOKEN = tokenGenerator
                .generateTokenPair(TEST_MEMBER1_ID)
                .accessToken();
        TEST_MEMBER2 = memberRepository.save(
                new Member(
                        TEST_MEMBER2_ID,
                        TEST_FAMILY_ID,
                        LocalDate.now(),
                        "member1", "https://profile.co.kr", "profile.co.kr",
                        LocalDateTime.now()
                )
        );
        TEST_MEMBER2_TOKEN = tokenGenerator
                .generateTokenPair(TEST_MEMBER2_ID)
                .accessToken();
        memberRepository.save(
                new Member(
                        TEST_MEMBER3_ID,
                        "",
                        LocalDate.now(),
                        "member1", "https://profile.co.kr", "profile.co.kr",
                        LocalDateTime.now()
                )
        );
    }


    @Nested
    class 금월의_가족구성원_월간_랭킹_조회 {
        @Test
        void 정상_조회() throws Exception {
            // given
            Post post1 = postRepository.save(new Post("1", TEST_MEMBER1_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img", "content"));
            postRepository.save(new Post("2", TEST_MEMBER1_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img", "content"));
            postRepository.save(new Post("3", TEST_MEMBER2_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img", "content"));

            String date = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/v1/view/main/family-ranking")
                            .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.month").value(ZonedDateTime.now().getMonthValue()))
                    .andExpect(jsonPath("$.firstRanker.profileImageUrl").value(TEST_MEMBER1.getProfileImgUrl()))
                    .andExpect(jsonPath("$.firstRanker.name").value(TEST_MEMBER1.getName()))
                    .andExpect(jsonPath("$.firstRanker.survivalCount").value(2))
                    .andExpect(jsonPath("$.secondRanker.profileImageUrl").value(TEST_MEMBER2.getProfileImgUrl()))
                    .andExpect(jsonPath("$.secondRanker.name").value(TEST_MEMBER2.getName()))
                    .andExpect(jsonPath("$.secondRanker.survivalCount").value(1))
                    .andExpect(jsonPath("$.thirdRanker").doesNotExist())
                    .andExpect(jsonPath("$.mostRecentSurvivalPostDate").value(date));
        }

        @Test
        void 업로드된_게시물이_없을때는_null로_반환한다() throws Exception {
            // given

            String date = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/v1/view/main/family-ranking")
                            .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.month").value(ZonedDateTime.now().getMonthValue()))
                    .andExpect(jsonPath("$.firstRanker").doesNotExist())
                    .andExpect(jsonPath("$.secondRanker").doesNotExist())
                    .andExpect(jsonPath("$.thirdRanker").doesNotExist())
                    .andExpect(jsonPath("$.mostRecentSurvivalPostDate").doesNotExist());
        }

        @Test
        void 금월에_업로드된_게시물이_없을때는_null로_반환한다() throws Exception {
            // given
            jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                    "1", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "SURVIVAL", "https://storage.com/images/1", 0, 0, "2023-11-01 14:00:00", "2023-11-02 14:00:00", "post1111", "1");
            jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                    "2", TEST_MEMBER2_ID, TEST_FAMILY_ID, 2, "SURVIVAL", "https://storage.com/images/2", 0, 0, "2023-11-01 14:00:00", "2023-11-02 14:00:00", "post2222", "2");

            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/v1/posts/ranking")
                            .param("type", "SURVIVAL")
                            .param("scope", "FAMILY")
                            .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.month").value(ZonedDateTime.now().getMonthValue()))
                    .andExpect(jsonPath("$.firstRanker").doesNotExist())
                    .andExpect(jsonPath("$.secondRanker").doesNotExist())
                    .andExpect(jsonPath("$.thirdRanker").doesNotExist())
                    .andExpect(jsonPath("$.mostRecentSurvivalPostDate").doesNotExist());
        }

        @Test
        void 게시글의_수가_같으면_댓글의_수를_비교한다() throws Exception {
            // given
            Post post1 = postRepository.save(new Post("1", TEST_MEMBER1_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img", "content"));
            postRepository.save(new Post("2", TEST_MEMBER2_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img", "content"));

            commentRepository.save(new Comment("1", post1, TEST_MEMBER2_ID, "content"));

            reactionRepository.save(new Reaction("1", post1, TEST_MEMBER1_ID, Emoji.EMOJI_1));
            reactionRepository.save(new Reaction("2", post1, TEST_MEMBER1_ID, Emoji.EMOJI_1));
            reactionRepository.save(new Reaction("3", post1, TEST_MEMBER1_ID, Emoji.EMOJI_1));

            String date = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/v1/view/main/family-ranking")
                            .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.month").value(ZonedDateTime.now().getMonthValue()))
                    .andExpect(jsonPath("$.firstRanker.profileImageUrl").value(TEST_MEMBER2.getProfileImgUrl()))
                    .andExpect(jsonPath("$.firstRanker.name").value(TEST_MEMBER2.getName()))
                    .andExpect(jsonPath("$.firstRanker.survivalCount").value(1))
                    .andExpect(jsonPath("$.secondRanker.profileImageUrl").value(TEST_MEMBER1.getProfileImgUrl()))
                    .andExpect(jsonPath("$.secondRanker.name").value(TEST_MEMBER1.getName()))
                    .andExpect(jsonPath("$.secondRanker.survivalCount").value(1))
                    .andExpect(jsonPath("$.thirdRanker").doesNotExist())
                    .andExpect(jsonPath("$.mostRecentSurvivalPostDate").value(date));
        }

        @Test
        void 게시물과_댓글의_수가_같으면_리액션의_수를_비교한다() throws Exception {
            // given
            Post post1 = postRepository.save(new Post("1", TEST_MEMBER1_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img", "content"));
            postRepository.save(new Post("2", TEST_MEMBER2_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img", "content"));

            commentRepository.save(new Comment("1", post1, TEST_MEMBER1_ID, "content"));
            commentRepository.save(new Comment("2", post1, TEST_MEMBER2_ID, "content"));

            reactionRepository.save(new Reaction("1", post1, TEST_MEMBER1_ID, Emoji.EMOJI_1));
            reactionRepository.save(new Reaction("2", post1, TEST_MEMBER2_ID, Emoji.EMOJI_1));
            reactionRepository.save(new Reaction("3", post1, TEST_MEMBER2_ID, Emoji.EMOJI_1));

            String date = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/v1/view/main/family-ranking")
                            .param("type", "SURVIVAL")
                            .param("scope", "FAMILY")
                            .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.month").value(ZonedDateTime.now().getMonthValue()))
                    .andExpect(jsonPath("$.firstRanker.profileImageUrl").value(TEST_MEMBER2.getProfileImgUrl()))
                    .andExpect(jsonPath("$.firstRanker.name").value(TEST_MEMBER2.getName()))
                    .andExpect(jsonPath("$.firstRanker.survivalCount").value(1))
                    .andExpect(jsonPath("$.secondRanker.profileImageUrl").value(TEST_MEMBER1.getProfileImgUrl()))
                    .andExpect(jsonPath("$.secondRanker.name").value(TEST_MEMBER1.getName()))
                    .andExpect(jsonPath("$.secondRanker.survivalCount").value(1))
                    .andExpect(jsonPath("$.thirdRanker").doesNotExist())
                    .andExpect(jsonPath("$.mostRecentSurvivalPostDate").value(date));
        }

        @Test
        void 게시물과_댓글과_리액션의_수가_같으면_아이디를_비교한다() throws Exception {
            // given
            Post post1 = postRepository.save(new Post("1", TEST_MEMBER1_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img", "content"));
            postRepository.save(new Post("2", TEST_MEMBER2_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img", "content"));

            commentRepository.save(new Comment("1", post1, TEST_MEMBER1_ID, "content"));
            commentRepository.save(new Comment("2", post1, TEST_MEMBER2_ID, "content"));

            reactionRepository.save(new Reaction("1", post1, TEST_MEMBER1_ID, Emoji.EMOJI_1));
            reactionRepository.save(new Reaction("2", post1, TEST_MEMBER2_ID, Emoji.EMOJI_1));

            String date = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/v1/view/main/family-ranking")
                            .param("type", "SURVIVAL")
                            .param("scope", "FAMILY")
                            .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.month").value(ZonedDateTime.now().getMonthValue()))
                    .andExpect(jsonPath("$.firstRanker.profileImageUrl").value(TEST_MEMBER1.getProfileImgUrl()))
                    .andExpect(jsonPath("$.firstRanker.name").value(TEST_MEMBER1.getName()))
                    .andExpect(jsonPath("$.firstRanker.survivalCount").value(1))
                    .andExpect(jsonPath("$.secondRanker.profileImageUrl").value(TEST_MEMBER2.getProfileImgUrl()))
                    .andExpect(jsonPath("$.secondRanker.name").value(TEST_MEMBER2.getName()))
                    .andExpect(jsonPath("$.secondRanker.survivalCount").value(1))
                    .andExpect(jsonPath("$.thirdRanker").doesNotExist())
                    .andExpect(jsonPath("$.mostRecentSurvivalPostDate").value(date));
        }
    }
}