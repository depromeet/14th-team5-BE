package com.oing.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.config.support.OptimizedImageUrlProvider;
import com.oing.domain.CreateNewUserDTO;
import com.oing.domain.Mission;
import com.oing.domain.SocialLoginProvider;
import com.oing.dto.request.CreateMissionRequest;
import com.oing.dto.request.JoinFamilyRequest;
import com.oing.dto.response.DeepLinkResponse;
import com.oing.dto.response.FamilyResponse;
import com.oing.service.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
class CalendarApiTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;
    @Autowired
    private MissionService missionService;
    @Autowired
    private DailyMissionHistoryService dailyMissionHistoryService;
    @Autowired
    private FamilyService familyService;
    @Autowired
    private DeepLinkService deepLinkService;
    @Autowired
    private TokenGenerator tokenGenerator;

    private String TEST_MEMBER1_ID;
    private String TEST_MEMBER1_TOKEN;
    private String TEST_MEMBER2_ID;
    private String TEST_MEMBER2_TOKEN;
    private String TEST_FAMILY_ID;
    private String TEST_MEMBER3_ID;
    private String TEST_MEMBER3_TOKEN;

    @Value("${cloud.ncp.image-optimizer-cdn}")
    private String imageOptimizerCdn;


    @BeforeEach
    void setUp() throws Exception {
        TEST_MEMBER1_ID = memberService.createNewMember(
                new CreateNewUserDTO(
                        SocialLoginProvider.fromString("APPLE"),
                        "testUser1",
                        "testUser1",
                        LocalDate.of(1999, 10, 18),
                        "https://bucket.com/image.jpg"
                )
        ).getId();
        TEST_MEMBER1_TOKEN = tokenGenerator.generateTokenPair(TEST_MEMBER1_ID).accessToken();

        TEST_MEMBER2_ID = memberService.createNewMember(
                new CreateNewUserDTO(
                        SocialLoginProvider.fromString("APPLE"),
                        "testUser2",
                        "testUser2",
                        LocalDate.of(2000, 10, 18),
                        "https://bucket.com/image.jpg"
                )
        ).getId();
        TEST_MEMBER2_TOKEN = tokenGenerator.generateTokenPair(TEST_MEMBER2_ID).accessToken();

        // family
        TEST_FAMILY_ID = objectMapper.readValue(
                mockMvc.perform(post("/v1/me/create-family").header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), FamilyResponse.class
        ).familyId();
        String inviteCode = objectMapper.readValue(
                mockMvc.perform(post("/v1/links/family/{familyId}", TEST_FAMILY_ID).header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), DeepLinkResponse.class
        ).getLinkId();
        mockMvc.perform(post("/v1/me/join-family")
                .header("X-AUTH-TOKEN", TEST_MEMBER2_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new JoinFamilyRequest(inviteCode)))
        ).andExpect(status().isOk());


        TEST_MEMBER3_ID = memberService.createNewMember(
                new CreateNewUserDTO(
                        SocialLoginProvider.fromString("APPLE"),
                        "testUser3",
                        "testUser3",
                        LocalDate.of(2001, 10, 18),
                        "https://bucket.com/image.jpg"
                )
        ).getId();
        TEST_MEMBER3_TOKEN = tokenGenerator.generateTokenPair(TEST_MEMBER3_ID).accessToken();

        entityManager.flush();
        jdbcTemplate.update("update member set family_join_at = ? where member_id = ?;", "2023-11-01 14:00:00", TEST_MEMBER1_ID);
        jdbcTemplate.update("update member set family_join_at = ? where member_id = ?;", "2023-11-02 14:00:00", TEST_MEMBER2_ID);
    }


    /*
     * 주어진 날의 게시글만 불러오는가?
     * 업로드날 짜 오름차순, 생존 게시물 미션 게시글 순서로 정렬되는가?
     */
    @Test
    void 일간_캘린더_조회_테스트() throws Exception {
        // Given
        // parameterss
        String yearMonthDay = "2023-11-02";

        // posts
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "1", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/1", 0, 0, "2023-11-01 14:00:00", "2023-11-02 14:00:00", "post1111", "1");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "2", TEST_MEMBER2_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/2", 0, 0, "2023-11-01 15:00:00", "2023-11-02 15:00:00", "post2222", "2");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "3", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/3", 0, 0, "2023-11-02 14:00:00", "2023-11-02 14:00:00", "post1111", "1");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "4", TEST_MEMBER2_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/4", 0, 0, "2023-11-02 15:00:00", "2023-11-02 15:00:00", "post2222", "2");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "5", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/5", 0, 0, "2023-11-03 14:00:00", "2023-11-02 14:00:00", "post1111", "1");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "6", TEST_MEMBER2_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/6", 0, 0, "2023-11-03 15:00:00", "2023-11-02 15:00:00", "post2222", "2");

        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "7", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "mission", "https://storage.com/images/7", 0, 0, "2023-11-02 14:00:00", "2023-11-02 14:00:00", "post1111", "1");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "8", TEST_MEMBER2_ID, TEST_FAMILY_ID, 1, "mission", "https://storage.com/images/8", 0, 0, "2023-11-02 15:00:00", "2023-11-02 15:00:00", "post2222", "2");

        // mission
        String missionId = missionService.createMission(new CreateMissionRequest("오늘의 기분을 나타내는 사진 찍기.")).id();
        Mission mission = missionService.getMissionByMissionId(missionId);
        dailyMissionHistoryService.createDailyMissionHistory(LocalDate.parse(yearMonthDay, DateTimeFormatter.ISO_DATE), mission);


        // When
        ResultActions result = mockMvc.perform(get("/v1/calendar/daily")
                .param("yearMonthDay", yearMonthDay)
                .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].date").value("2023-11-02"))
                .andExpect(jsonPath("$.results[0].type").value("survival"))
                .andExpect(jsonPath("$.results[0].postId").value("3"))
                .andExpect(jsonPath("$.results[0].postImgUrl").value("https://storage.com/images/3"))
                .andExpect(jsonPath("$.results[0].postContent").value("post1111"))
                .andExpect(jsonPath("$.results[0].missionContent").isEmpty())
                .andExpect(jsonPath("$.results[0].authorId").value(TEST_MEMBER1_ID))
                .andExpect(jsonPath("$.results[0].commentCount").value(0))
                .andExpect(jsonPath("$.results[0].emojiCount").value(0))
                .andExpect(jsonPath("$.results[0].allFamilyMembersUploaded").value(true))
                .andExpect(jsonPath("$.results[1].date").value("2023-11-02"))
                .andExpect(jsonPath("$.results[1].type").value("survival"))
                .andExpect(jsonPath("$.results[1].postId").value("4"))
                .andExpect(jsonPath("$.results[1].postImgUrl").value("https://storage.com/images/4"))
                .andExpect(jsonPath("$.results[1].postContent").value("post2222"))
                .andExpect(jsonPath("$.results[1].missionContent").isEmpty())
                .andExpect(jsonPath("$.results[1].authorId").value(TEST_MEMBER2_ID))
                .andExpect(jsonPath("$.results[1].commentCount").value(0))
                .andExpect(jsonPath("$.results[1].emojiCount").value(0))
                .andExpect(jsonPath("$.results[1].allFamilyMembersUploaded").value(true))
                .andExpect(jsonPath("$.results[2].date").value("2023-11-02"))
                .andExpect(jsonPath("$.results[2].type").value("mission"))
                .andExpect(jsonPath("$.results[2].postId").value("7"))
                .andExpect(jsonPath("$.results[2].postImgUrl").value("https://storage.com/images/7"))
                .andExpect(jsonPath("$.results[2].postContent").value("post1111"))
                .andExpect(jsonPath("$.results[2].missionContent").value("오늘의 기분을 나타내는 사진 찍기."))
                .andExpect(jsonPath("$.results[2].authorId").value(TEST_MEMBER1_ID))
                .andExpect(jsonPath("$.results[2].commentCount").value(0))
                .andExpect(jsonPath("$.results[2].emojiCount").value(0))
                .andExpect(jsonPath("$.results[2].allFamilyMembersUploaded").value(true))
                .andExpect(jsonPath("$.results[3].date").value("2023-11-02"))
                .andExpect(jsonPath("$.results[3].type").value("mission"))
                .andExpect(jsonPath("$.results[3].postId").value("8"))
                .andExpect(jsonPath("$.results[3].postImgUrl").value("https://storage.com/images/8"))
                .andExpect(jsonPath("$.results[3].postContent").value("post2222"))
                .andExpect(jsonPath("$.results[3].missionContent").value("오늘의 기분을 나타내는 사진 찍기."))
                .andExpect(jsonPath("$.results[3].authorId").value(TEST_MEMBER2_ID))
                .andExpect(jsonPath("$.results[3].commentCount").value(0))
                .andExpect(jsonPath("$.results[3].emojiCount").value(0))
                .andExpect(jsonPath("$.results[3].allFamilyMembersUploaded").value(true));

    }

    @Test
    void 캘린더의_게시글_정책에_적합한_일간_캘린더_조회_테스트() throws Exception {
        // Given
        // parameterss
        String yearMonthDay = "2023-11-02";

        // posts
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "1", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/3", 0, 0, "2023-11-02 14:01:00", "2023-11-02 14:01:00", "post1111", "1");

        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "2", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "mission", "https://storage.com/images/7", 0, 0, "2023-11-02 14:02:00", "2023-11-02 14:02:00", "post1111", "1");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "3", TEST_MEMBER2_ID, TEST_FAMILY_ID, 1, "mission", "https://storage.com/images/8", 0, 0, "2023-11-02 15:03:00", "2023-11-02 15:03:00", "post2222", "2");

        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "4", TEST_MEMBER2_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/4", 0, 0, "2023-11-02 15:04:00", "2023-11-02 15:04:00", "post2222", "2");

        // mission
        String missionId = missionService.createMission(new CreateMissionRequest("오늘의 기분을 나타내는 사진 찍기.")).id();
        Mission mission = missionService.getMissionByMissionId(missionId);
        dailyMissionHistoryService.createDailyMissionHistory(LocalDate.parse(yearMonthDay, DateTimeFormatter.ISO_DATE), mission);


        // When
        ResultActions result = mockMvc.perform(get("/v1/calendar/daily")
                .param("yearMonthDay", yearMonthDay)
                .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].postId").value("1"))
                .andExpect(jsonPath("$.results[1].postId").value("4"))
                .andExpect(jsonPath("$.results[2].postId").value("2"))
                .andExpect(jsonPath("$.results[3].postId").value("3"));

    }

    @Test
    void 일간_캘린더_조회시_가족구성원_전체_업로드_여부_테스트() throws Exception {
        // Given
        // parameterss
        String yearMonthDay = "2023-11-02";

        // posts
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "1", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/3", 0, 0, "2023-11-02 14:01:00", "2023-11-02 14:01:00", "post1111", "1");

        // mission
        String missionId = missionService.createMission(new CreateMissionRequest("오늘의 기분을 나타내는 사진 찍기.")).id();
        Mission mission = missionService.getMissionByMissionId(missionId);
        dailyMissionHistoryService.createDailyMissionHistory(LocalDate.parse(yearMonthDay, DateTimeFormatter.ISO_DATE), mission);


        // When
        ResultActions result = mockMvc.perform(get("/v1/calendar/daily")
                .param("yearMonthDay", yearMonthDay)
                .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].postId").value("1"))
                .andExpect(jsonPath("$.results[0].allFamilyMembersUploaded").value(false));

    }


    @Test
    void 월간_캘린더_조회_테스트() throws Exception {
        // Given
        // parameterss
        String yearMonth = "2023-11";

        // posts
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "1", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/1", 0, 0, "2023-11-02 14:00:00", "2023-11-02 14:00:00", "post1111", "1");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "2", TEST_MEMBER2_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/2", 0, 0, "2023-11-02 15:00:00", "2023-11-02 15:00:00", "post2222", "2");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "3", TEST_MEMBER3_ID, "something_other", 1, "survival", "https://storage.com/images/3", 0, 0, "2023-11-02 17:00:00", "2023-11-02 17:00:00", "post3333", "3");

        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "4", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/4", 0, 0, "2023-11-03 14:00:00", "2023-11-03 14:00:00", "post4444", "4");


        // When & Then
        mockMvc.perform(get("/v1/calendar/monthly")
                        .param("yearMonth", yearMonth)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].date").value("2023-11-02"))
                .andExpect(jsonPath("$.results[0].representativePostId").value("2"))
                .andExpect(jsonPath("$.results[0].representativeThumbnailUrl").value(imageOptimizerCdn + "/images/2" + OptimizedImageUrlProvider.THUMBNAIL_OPTIMIZER_QUERY_STRING))
                .andExpect(jsonPath("$.results[0].allFamilyMembersUploaded").value(true))
                .andExpect(jsonPath("$.results[1].date").value("2023-11-03"))
                .andExpect(jsonPath("$.results[1].representativePostId").value("4"))
                .andExpect(jsonPath("$.results[1].representativeThumbnailUrl").value(imageOptimizerCdn + "/images/4" + OptimizedImageUrlProvider.THUMBNAIL_OPTIMIZER_QUERY_STRING))
                .andExpect(jsonPath("$.results[1].allFamilyMembersUploaded").value(false));

    }


    @Test
    void 월간_캘린더_뒤늦게_가족에_가입한_멤버를_고려한_조회_테스트() throws Exception {
        // parameters
        String yearMonth = "2023-11";

        // posts
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "0", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/0", 0, 0, "2023-11-01 14:00:00", "2023-11-01 14:00:00", "post0000", "0");

        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "1", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/1", 0, 0, "2023-11-02 14:00:00", "2023-11-02 14:00:00", "post1111", "1");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "2", TEST_MEMBER2_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/2", 0, 0, "2023-11-02 15:00:00", "2023-11-02 15:00:00", "post2222", "2");

        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "3", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/3", 0, 0, "2023-11-03 13:00:00", "2023-11-03 13:00:00", "post3333", "3");


        // When & Then
        mockMvc.perform(get("/v1/calendar/monthly")
                        .param("yearMonth", yearMonth)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].date").value("2023-11-01"))
                .andExpect(jsonPath("$.results[0].representativePostId").value("0"))
                .andExpect(jsonPath("$.results[0].representativeThumbnailUrl").value(imageOptimizerCdn + "/images/0" + OptimizedImageUrlProvider.THUMBNAIL_OPTIMIZER_QUERY_STRING))
                .andExpect(jsonPath("$.results[0].allFamilyMembersUploaded").value(true))
                .andExpect(jsonPath("$.results[1].date").value("2023-11-02"))
                .andExpect(jsonPath("$.results[1].representativePostId").value("2"))
                .andExpect(jsonPath("$.results[1].representativeThumbnailUrl").value(imageOptimizerCdn + "/images/2" + OptimizedImageUrlProvider.THUMBNAIL_OPTIMIZER_QUERY_STRING))
                .andExpect(jsonPath("$.results[1].allFamilyMembersUploaded").value(true))
                .andExpect(jsonPath("$.results[2].date").value("2023-11-03"))
                .andExpect(jsonPath("$.results[2].representativePostId").value("3"))
                .andExpect(jsonPath("$.results[2].representativeThumbnailUrl").value(imageOptimizerCdn + "/images/3" + OptimizedImageUrlProvider.THUMBNAIL_OPTIMIZER_QUERY_STRING))
                .andExpect(jsonPath("$.results[2].allFamilyMembersUploaded").value(false));
    }

    @Test
    void 월간_캘린더_가족을_탈퇴한_멤버를_고려한_조회_테스트() throws Exception {
        // Given
        // parameters
        String yearMonth = "2023-11";

        // posts
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "1", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/1", 0, 0, "2023-11-02 14:00:00", "2023-11-02 14:00:00", "post1111", "1");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "2", TEST_MEMBER2_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/2", 0, 0, "2023-11-02 15:00:00", "2023-11-02 15:00:00", "post2222", "2");

        // Member2 가족 탈퇴
        mockMvc.perform(post("/v1/me/quit-family")
                .header("X-AUTH-TOKEN", TEST_MEMBER2_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        // Member3 가족 가입
        String inviteCode = objectMapper.readValue(
                mockMvc.perform(post("/v1/links/family/{familyId}", TEST_FAMILY_ID).header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), DeepLinkResponse.class
        ).getLinkId();
        mockMvc.perform(post("/v1/me/join-family")
                .header("X-AUTH-TOKEN", TEST_MEMBER3_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new JoinFamilyRequest(inviteCode)))
        ).andExpect(status().isOk());
        entityManager.flush();
        jdbcTemplate.update("update member set family_join_at = ? where member_id = ?;", "2023-11-02 14:00:00", TEST_MEMBER3_ID);


        // When & Then
        mockMvc.perform(get("/v1/calendar/monthly")
                        .param("yearMonth", yearMonth)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].date").value("2023-11-02"))
                .andExpect(jsonPath("$.results[0].representativePostId").value("2"))
                .andExpect(jsonPath("$.results[0].representativeThumbnailUrl").value(imageOptimizerCdn + "/images/2" + OptimizedImageUrlProvider.THUMBNAIL_OPTIMIZER_QUERY_STRING))
                .andExpect(jsonPath("$.results[0].allFamilyMembersUploaded").value(false));
    }


    @Test
    void 캘린더_배너_조회_태스트() throws Exception {
        // parameters
        String yearMonth = "2023-11";

        // posts
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "1", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/1", 0, 0, "2023-11-01 14:00:00", "2023-11-01 14:00:00", "post1111", "1");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "2", TEST_MEMBER2_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/2", 0, 0, "2023-11-01 15:00:00", "2023-11-01 15:00:00", "post2222", "2");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "3", TEST_MEMBER3_ID, "something_other", 1, "survival", "https://storage.com/images/3", 0, 0, "2023-11-01 17:00:00", "2023-11-01 17:00:00", "post3333", "3");

        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "4", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/4", 0, 0, "2023-11-02 14:00:00", "2023-11-02 14:00:00", "post4444", "4");

        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "5", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/5", 0, 0, "2023-11-29 14:00:00", "2023-11-29 14:00:00", "post5555", "5");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "6", TEST_MEMBER2_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/6", 0, 0, "2023-11-29 15:00:00", "2023-11-29 15:00:00", "post6666", "6");
        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "7", TEST_MEMBER3_ID, "something_other", 1, "survival", "https://storage.com/images/7", 0, 0, "2023-11-29 17:00:00", "2023-11-29 17:00:00", "post7777", "7");

        jdbcTemplate.update("insert into post (post_id, member_id, family_id, mission_id, type, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "8", TEST_MEMBER1_ID, TEST_FAMILY_ID, 1, "survival", "https://storage.com/images/8", 0, 0, "2023-11-30 14:00:00", "2023-11-30 14:00:00", "post8888", "8");


        // When & Then
        mockMvc.perform(get("/v1/calendar/banner")
                        .param("yearMonth", yearMonth)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.familyTopPercentage").isNumber())
                .andExpect(jsonPath("$.allFamilyMembersUploadedDays").isNumber());
    }
}