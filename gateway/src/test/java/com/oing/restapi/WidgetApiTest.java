package com.oing.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.config.support.OptimizedImageUrlProvider;
import com.oing.domain.CreateNewUserDTO;
import com.oing.domain.Member;
import com.oing.domain.MemberPost;
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
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
class WidgetApiTest {

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


    private Member TEST_MEMBER1;
    private String TEST_MEMBER1_TOKEN;
    private Member TEST_MEMBER2;
    private String TEST_MEMBER2_TOKEN;
    private Member TEST_MEMBER3;
    private String TEST_MEMBER3_TOKEN;
    private List<String> TEST_FAMILIES_IDS;


    @Value("${cloud.ncp.image-optimizer-cdn}")
    private String imageOptimizerCdn;


    @BeforeEach
    void setUp() throws Exception {
        TEST_MEMBER1 = memberService.createNewMember(
                new CreateNewUserDTO(
                        SocialLoginProvider.fromString("APPLE"),
                        "testUser1",
                        "testUser1",
                        LocalDate.of(1999, 10, 18),
                        "https://storage.com/bucket/images/1"
                )
        );
        TEST_MEMBER1_TOKEN = tokenGenerator.generateTokenPair(TEST_MEMBER1.getId()).accessToken();

        TEST_MEMBER2 = memberService.createNewMember(
                new CreateNewUserDTO(
                        SocialLoginProvider.fromString("APPLE"),
                        "testUser2",
                        "testUser2",
                        LocalDate.of(2000, 10, 18),
                        "https://storage.com/bucket/images/2"
                )
        );
        TEST_MEMBER2_TOKEN = tokenGenerator.generateTokenPair(TEST_MEMBER2.getId()).accessToken();

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

        TEST_MEMBER3 = memberService.createNewMember(
                new CreateNewUserDTO(
                        SocialLoginProvider.fromString("APPLE"),
                        "testUser3",
                        "testUser3",
                        LocalDate.of(2001, 10, 18),
                        "https://storage.com/bucket/images/3"
                )
        );
        TEST_MEMBER3_TOKEN = tokenGenerator.generateTokenPair(TEST_MEMBER3.getId()).accessToken();

        TEST_FAMILIES_IDS = List.of(TEST_MEMBER1.getId(), TEST_MEMBER2.getId());
    }


    @Test
    void 최근_게시글_싱글_위젯_정상_조회_테스트() throws Exception {
        // given
        MemberPost testPost1 = new MemberPost(
                "testPost1",
                TEST_MEMBER1.getId(),
                "https://storage.com/bucket/images/1",
                "1",
                "testPos1"
        );
        MemberPost testPost2 = new MemberPost(
                "testPost2",
                TEST_MEMBER2.getId(),
                "https://storage.com/bucket/images/2",
                "2",
                "testPos2"
        );
        MemberPost testPost3 = new MemberPost(
                "testPost3",
                TEST_MEMBER3.getId(),
                "https://storage.com/bucket/images/3",
                "3",
                "testPos3"
        );
        memberPostService.save(testPost1);
        memberPostService.save(testPost2);
        memberPostService.save(testPost3);


        // when & then
        mockMvc.perform(get("/v1/widgets/single-recent-family-post")
                        .param("date", LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName").value(TEST_MEMBER2.getName()))
                .andExpect(jsonPath("$.authorProfileImageUrl").value(imageOptimizerCdn + "/images/2" + OptimizedImageUrlProvider.KB_IMAGE_OPTIMIZER_QUERY_STRING))
                .andExpect(jsonPath("$.postImageUrl").value(imageOptimizerCdn + "/images/2" + OptimizedImageUrlProvider.KB_IMAGE_OPTIMIZER_QUERY_STRING))
                .andExpect(jsonPath("$.postContent").value(testPost2.getContent()));

    }


    @Test
    void 최근_게시글_싱글_위젯_파라미터_없이_조회_테스트() throws Exception {
        // given
        MemberPost testPost1 = new MemberPost(
                "testPost1",
                TEST_MEMBER1.getId(),
                "https://storage.com/bucket/images/1",
                "1",
                "testPos1"
        );
        MemberPost testPost2 = new MemberPost(
                "testPost2",
                TEST_MEMBER2.getId(),
                "https://storage.com/bucket/images/2",
                "2",
                "testPos2"
        );
        MemberPost testPost3 = new MemberPost(
                "testPost3",
                TEST_MEMBER3.getId(),
                "https://storage.com/bucket/images/3",
                "3",
                "testPos3"
        );
        memberPostService.save(testPost1);
        memberPostService.save(testPost2);
        memberPostService.save(testPost3);


        // when & then
        mockMvc.perform(get("/v1/widgets/single-recent-family-post")
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName").value(TEST_MEMBER2.getName()))
                .andExpect(jsonPath("$.authorProfileImageUrl").value(imageOptimizerCdn + "/images/2" + OptimizedImageUrlProvider.KB_IMAGE_OPTIMIZER_QUERY_STRING))
                .andExpect(jsonPath("$.postImageUrl").value(imageOptimizerCdn + "/images/2" + OptimizedImageUrlProvider.KB_IMAGE_OPTIMIZER_QUERY_STRING))
                .andExpect(jsonPath("$.postContent").value(testPost2.getContent()));

    }


    @Test
    void 최근_게시글_싱글_위젯_게시글_없이_조회_테스트() throws Exception {
        // when & then
        mockMvc.perform(get("/v1/widgets/single-recent-family-post")
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                )
                .andExpect(status().isNoContent());

    }
}
