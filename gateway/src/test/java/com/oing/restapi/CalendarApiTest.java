package com.oing.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.domain.CreateNewUserDTO;
import com.oing.domain.SocialLoginProvider;
import com.oing.dto.request.JoinFamilyRequest;
import com.oing.dto.response.DeepLinkResponse;
import com.oing.dto.response.FamilyResponse;
import com.oing.service.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

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
    private MemberService memberService;
    @Autowired
    private MemberPostService memberPostService;
    @Autowired
    private FamilyService familyService;
    @Autowired
    private DeepLinkService deepLinkService;
    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String TEST_MEMBER1_ID;
    private String TEST_MEMBER1_TOKEN;
    private String TEST_MEMBER2_ID;
    private String TEST_MEMBER2_TOKEN;
    private String TEST_MEMBER3_ID;
    private String TEST_MEMBER3_TOKEN;
    private List<String> TEST_FAMILIES_IDS;

    @Value("${cloud.ncp.image-optimizer-cdn}")
    private String imageOptimizerCdn;
    @Value("${cloud.ncp.thumbnail-optimizer-query}")
    private String thumbnailOptimizerQuery;


    @BeforeEach
    void setUp() {
        TEST_MEMBER1_ID = memberService.createNewMember(
                new CreateNewUserDTO(
                        SocialLoginProvider.fromString("APPLE"),
                        "testUser1",
                        "testUser1",
                        LocalDate.of(1999, 10, 18),
                        "profile.com"
                )
        ).getId();
        TEST_MEMBER1_TOKEN = tokenGenerator.generateTokenPair(TEST_MEMBER1_ID).accessToken();

        TEST_MEMBER2_ID = memberService.createNewMember(
                new CreateNewUserDTO(
                        SocialLoginProvider.fromString("APPLE"),
                        "testUser2",
                        "testUser2",
                        LocalDate.of(2000, 10, 18),
                        "profile.com"
                )
        ).getId();
        TEST_MEMBER2_TOKEN = tokenGenerator.generateTokenPair(TEST_MEMBER2_ID).accessToken();

        TEST_MEMBER3_ID = memberService.createNewMember(
                new CreateNewUserDTO(
                        SocialLoginProvider.fromString("APPLE"),
                        "testUser3",
                        "testUser3",
                        LocalDate.of(2001, 10, 18),
                        "profile.com"
                )
        ).getId();
        TEST_MEMBER3_TOKEN = tokenGenerator.generateTokenPair(TEST_MEMBER3_ID).accessToken();

        TEST_FAMILIES_IDS = List.of(TEST_MEMBER1_ID, TEST_MEMBER2_ID);
    }


    @Test
    void 월별_캘린더_조회_테스트() throws Exception {
        // Given
        // parameters
        String yearMonth = "2023-11";

        // posts
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('1', '" + TEST_MEMBER1_ID + "', 'https://storage.com/images/1', 0, 0, '2023-11-01 14:00:00', '2023-11-01 14:00:00', 'post1111', '1');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('2', '" + TEST_MEMBER2_ID + "', 'https://storage.com/images/2', 0, 0, '2023-11-01 15:00:00', '2023-11-01 15:00:00', 'post2222', '2');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('3', '" + TEST_MEMBER3_ID + "', 'https://storage.com/images/3', 0, 0, '2023-11-01 17:00:00', '2023-11-01 17:00:00', 'post3333', '3');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('4', '" + TEST_MEMBER1_ID + "', 'https://storage.com/images/4', 0, 0, '2023-11-02 14:00:00', '2023-11-02 14:00:00', 'post4444', '4');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('5', '" + TEST_MEMBER1_ID + "', 'https://storage.com/images/5', 0, 0, '2023-11-29 14:00:00', '2023-11-29 14:00:00', 'post5555', '5');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('6', '" + TEST_MEMBER2_ID + "', 'https://storage.com/images/6', 0, 0, '2023-11-29 15:00:00', '2023-11-29 15:00:00', 'post6666', '6');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('7', '" + TEST_MEMBER3_ID + "', 'https://storage.com/images/7', 0, 0, '2023-11-29 17:00:00', '2023-11-29 17:00:00', 'post7777', '7');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('8', '" + TEST_MEMBER1_ID + "', 'https://storage.com/images/8', 0, 0, '2023-11-30 14:00:00', '2023-11-30 14:00:00', 'post8888', '8');");

        // family
        String familyId = objectMapper.readValue(
                mockMvc.perform(post("/v1/me/create-family").header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), FamilyResponse.class
        ).familyId();
        String inviteCode = objectMapper.readValue(
                mockMvc.perform(post("/v1/links/family/{familyId}", familyId).header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), DeepLinkResponse.class
        ).getLinkId();
        mockMvc.perform(post("/v1/me/join-family")
                .header("X-AUTH-TOKEN", TEST_MEMBER2_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new JoinFamilyRequest(inviteCode)))
        ).andExpect(status().isOk());


        // When & Then
        mockMvc.perform(get("/v1/calendar")
                        .param("type", "MONTHLY")
                        .param("yearMonth", yearMonth)
                        .param("familyId", familyId)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].date").value("2023-11-01"))
                .andExpect(jsonPath("$.results[0].representativePostId").value("2"))
                .andExpect(jsonPath("$.results[0].representativeThumbnailUrl").value(imageOptimizerCdn + "/images/2" + thumbnailOptimizerQuery))
                .andExpect(jsonPath("$.results[0].allFamilyMembersUploaded").value(true))
                .andExpect(jsonPath("$.results[1].date").value("2023-11-02"))
                .andExpect(jsonPath("$.results[1].representativePostId").value("4"))
                .andExpect(jsonPath("$.results[1].representativeThumbnailUrl").value(imageOptimizerCdn + "/images/4" + thumbnailOptimizerQuery))
                .andExpect(jsonPath("$.results[1].allFamilyMembersUploaded").value(false))
                .andExpect(jsonPath("$.results[2].date").value("2023-11-29"))
                .andExpect(jsonPath("$.results[2].representativePostId").value("6"))
                .andExpect(jsonPath("$.results[2].representativeThumbnailUrl").value(imageOptimizerCdn + "/images/6" + thumbnailOptimizerQuery))
                .andExpect(jsonPath("$.results[2].allFamilyMembersUploaded").value(true))
                .andExpect(jsonPath("$.results[3].date").value("2023-11-30"))
                .andExpect(jsonPath("$.results[3].representativePostId").value("8"))
                .andExpect(jsonPath("$.results[3].representativeThumbnailUrl").value(imageOptimizerCdn + "/images/8" + thumbnailOptimizerQuery))
                .andExpect(jsonPath("$.results[3].allFamilyMembersUploaded").value(false));

    }

    @Test
    void 캘린더_배너_조회_태스트() throws Exception {
        // parameters
        String yearMonth = "2023-11";

        // posts
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('1', '" + TEST_MEMBER1_ID + "', 'https://storage.com/images/1', 0, 0, '2023-11-01 14:00:00', '2023-11-01 14:00:00', 'post1111', '1');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('2', '" + TEST_MEMBER2_ID + "', 'https://storage.com/images/2', 0, 0, '2023-11-01 15:00:00', '2023-11-01 15:00:00', 'post2222', '2');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('3', '" + TEST_MEMBER3_ID + "', 'https://storage.com/images/3', 0, 0, '2023-11-01 17:00:00', '2023-11-01 17:00:00', 'post3333', '3');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('4', '" + TEST_MEMBER1_ID + "', 'https://storage.com/images/4', 0, 0, '2023-11-02 14:00:00', '2023-11-02 14:00:00', 'post4444', '4');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('5', '" + TEST_MEMBER1_ID + "', 'https://storage.com/images/5', 0, 0, '2023-11-29 14:00:00', '2023-11-29 14:00:00', 'post5555', '5');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('6', '" + TEST_MEMBER2_ID + "', 'https://storage.com/images/6', 0, 0, '2023-11-29 15:00:00', '2023-11-29 15:00:00', 'post6666', '6');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('7', '" + TEST_MEMBER3_ID + "', 'https://storage.com/images/7', 0, 0, '2023-11-29 17:00:00', '2023-11-29 17:00:00', 'post7777', '7');");
        jdbcTemplate.execute("insert into member_post (post_id, member_id, post_img_url, comment_cnt, reaction_cnt, created_at, updated_at, content, post_img_key) " +
                "values ('8', '" + TEST_MEMBER1_ID + "', 'https://storage.com/images/8', 0, 0, '2023-11-30 14:00:00', '2023-11-30 14:00:00', 'post8888', '8');");

        // family
        String familyId = objectMapper.readValue(
                mockMvc.perform(post("/v1/me/create-family").header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), FamilyResponse.class
        ).familyId();
        String inviteCode = objectMapper.readValue(
                mockMvc.perform(post("/v1/links/family/{familyId}", familyId).header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), DeepLinkResponse.class
        ).getLinkId();
        mockMvc.perform(post("/v1/me/join-family")
                .header("X-AUTH-TOKEN", TEST_MEMBER2_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new JoinFamilyRequest(inviteCode)))
        ).andExpect(status().isOk());


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