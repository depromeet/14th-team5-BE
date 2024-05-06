package com.oing.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.domain.*;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.repository.CommentRepository;
import com.oing.repository.PostRepository;
import com.oing.repository.MemberRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PostApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private ObjectMapper objectMapper;

    private String TEST_MEMBER1_ID = "01HGW2N7EHJVJ4CJ999RRS2E91";
    private String TEST_MEMBER2_ID = "01HGW2N7EHJVJ4CJ999RRS2E99";
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
        memberRepository.save(
                new Member(
                        TEST_MEMBER1_ID,
                        TEST_FAMILY_ID,
                        LocalDate.now(),
                        "", "", "",
                        LocalDateTime.now().minusDays(1)
                )
        );
        TEST_MEMBER1_TOKEN = tokenGenerator
                .generateTokenPair(TEST_MEMBER1_ID)
                .accessToken();
        memberRepository.save(
                new Member(
                        TEST_MEMBER2_ID,
                        TEST_FAMILY_ID,
                        LocalDate.now(),
                        "", "", "",
                        LocalDateTime.now().minusDays(1)
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
                        "", "", "",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void 게시물_이미지_업로드_URL_요청_테스트() throws Exception {
        //given
        String imageName = "feed.jpg";

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/v1/posts/image-upload-request")
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PreSignedUrlRequest(imageName)))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").exists());
    }

    @Test
    void 게시물_추가_테스트() throws Exception {
        //given
        CreatePostRequest request = new CreatePostRequest("https://test.com/bucket/images/feed.jpg",
                "content", ZonedDateTime.now());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/v1/posts")
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorId").value(TEST_MEMBER1_ID))
                .andExpect(jsonPath("$.imageUrl").value(request.imageUrl()))
                .andExpect(jsonPath("$.content").value(request.content()));
    }

    @Test
    void 그룹에서_탈퇴한_회원_게시물_조회_테스트() throws Exception {
        //given
        postRepository.save(new Post(TEST_POST_ID, TEST_MEMBER1_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img",
                "content"));
        mockMvc.perform(
                post("/v1/me/quit-family")
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/posts")
                        .header("X-AUTH-TOKEN", TEST_MEMBER2_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(1))
                .andExpect(jsonPath("$.results[0].postId").value(TEST_POST_ID))
                .andExpect(jsonPath("$.results[0].authorId").value(TEST_MEMBER1_ID))
                .andExpect(jsonPath("$.results[0].content").value("content"));
    }

    @Test
    void 생존게시글_올린_사람이_없을_때_미션_키_획득을_위해_앞으로_올려야_하는_생존게시글_수_조회_테스트() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/posts/{memberId}/remaining-survival-count", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.leftUploadCountUntilMissionUnlock").value(1));
    }

    @Test
    void 다른_구성원이_생존게시글_올렸을_때_미션_키_획득을_위해_앞으로_올려야_하는_생존게시글_수_조회_테스트() throws Exception {
        //given
        postRepository.save(new Post("1", TEST_MEMBER2_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img",
                "content"));

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/posts/{memberId}/remaining-survival-count", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.leftUploadCountUntilMissionUnlock").value(0));
    }

    @Test
    void 회원_생존신고_게시글_업로드_여부_조회_테스트() throws Exception {
        //given
        postRepository.save(new Post(TEST_POST_ID, TEST_MEMBER1_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img",
                "content"));

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/posts/{memberId}/survival-uploaded", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMeSurvivalUploadedToday").value(true));
    }

    @Test
    void 회원_미션_게시글_업로드_여부_조회_테스트() throws Exception {
        //given
        postRepository.save(new Post(TEST_POST_ID, TEST_MEMBER1_ID, TEST_FAMILY_ID, PostType.MISSION, "img", "img",
                "content"));

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/posts/{memberId}/mission-uploaded", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMeMissionUploadedToday").value(true));
    }

    @Test
    void 해당_가족의_미션_키_획득_여부_조회_테스트() throws Exception {
        //given
        postRepository.save(new Post(TEST_POST_ID, TEST_MEMBER1_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img",
                "content"));

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/posts/{memberId}/mission-available", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMissionUnlocked").value(true));
    }

    @Nested
    class 가족구성원들의_생존신고_랭킹_조회 {
        @Test
        void 정상_조회() throws Exception {
            // given
            Post post1 = postRepository.save(new Post("1", TEST_MEMBER1_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img", "content"));
            postRepository.save(new Post("2", TEST_MEMBER1_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img", "content"));
            postRepository.save(new Post("3", TEST_MEMBER2_ID, TEST_FAMILY_ID, PostType.SURVIVAL, "img", "img", "content"));

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
                    .andExpect(jsonPath("$.results[0].memberId").value(TEST_MEMBER1_ID))
                    .andExpect(jsonPath("$.results[0].postCount").value(2))
                    .andExpect(jsonPath("$.results[1].memberId").value(TEST_MEMBER2_ID))
                    .andExpect(jsonPath("$.results[1].postCount").value(1))
                    .andExpect(jsonPath("$.results[2]").doesNotExist());
        }

        @Test
        void 업로드된_게시물이_없을때는_빈_결과값을_반환한다() throws Exception {
            // given

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
                    .andExpect(jsonPath("$.results").isEmpty());
        }

        @Test
        void 금월에_업로드된_게시물이_없을때는_빈_결과값을_반환한다() throws Exception {
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
                    .andExpect(jsonPath("$.results").isEmpty());
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
                    .andExpect(jsonPath("$.results[0].memberId").value(TEST_MEMBER2_ID))
                    .andExpect(jsonPath("$.results[0].postCount").value(1))
                    .andExpect(jsonPath("$.results[1].memberId").value(TEST_MEMBER1_ID))
                    .andExpect(jsonPath("$.results[1].postCount").value(1))
                    .andExpect(jsonPath("$.results[2]").doesNotExist());
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
                    .andExpect(jsonPath("$.results[0].memberId").value(TEST_MEMBER2_ID))
                    .andExpect(jsonPath("$.results[0].postCount").value(1))
                    .andExpect(jsonPath("$.results[1].memberId").value(TEST_MEMBER1_ID))
                    .andExpect(jsonPath("$.results[1].postCount").value(1))
                    .andExpect(jsonPath("$.results[2]").doesNotExist());
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
                    .andExpect(jsonPath("$.results[0].memberId").value(TEST_MEMBER1_ID))
                    .andExpect(jsonPath("$.results[0].postCount").value(1))
                    .andExpect(jsonPath("$.results[1].memberId").value(TEST_MEMBER2_ID))
                    .andExpect(jsonPath("$.results[1].postCount").value(1))
                    .andExpect(jsonPath("$.results[2]").doesNotExist());
        }
    }
}
