package com.oing.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.domain.Member;
import com.oing.domain.MemberQuitReasonType;
import com.oing.dto.request.QuitMemberRequest;
import com.oing.dto.request.UpdateMemberNameRequest;
import com.oing.dto.request.UpdateMemberProfileImageUrlRequest;
import com.oing.repository.MemberRepository;
import com.oing.service.TokenGenerator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MemberApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;

    private String TEST_MEMBER1_ID = "id1";
    private String TEST_MEMBER1_TOKEN;
    private String TEST_MEMBER2_ID = "id2";
    private String TEST_MEMBER2_TOKEN;
    private String TEST_FAMILY_ID = "family1";
    private String TEST_MEMBER3_ID = "id3";
    private String TEST_MEMBER3_TOKEN;



    @BeforeEach
    void setUp() {
        memberRepository.save(
                new Member(
                        TEST_MEMBER1_ID,
                        TEST_FAMILY_ID,
                        LocalDate.now(),
                        "개발자", "http://test.com/test-profile.jpg", "/test-profile.jpg",
                        LocalDateTime.now()
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
                        "삐삐", null, null,
                        LocalDateTime.now()
                )
        );
        TEST_MEMBER2_TOKEN = tokenGenerator
                .generateTokenPair(TEST_MEMBER2_ID)
                .accessToken();

        memberRepository.save(
                new Member(
                        TEST_MEMBER3_ID,
                        "different-family",
                        LocalDate.now(),
                        "타인", "http://test.com/test-profile.jpg", "/test-profile.jpg",
                        LocalDateTime.now()
                )
        );
        TEST_MEMBER3_TOKEN = tokenGenerator
                .generateTokenPair(TEST_MEMBER3_ID)
                .accessToken();
    }

    @Test
    void 가족_회원_프로필_조회_테스트() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/members")
                        .param("type", "FAMILY")
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].memberId").value(TEST_MEMBER1_ID))
                .andExpect(jsonPath("$.results[1].memberId").value(TEST_MEMBER2_ID));
    }

    @Test
    void 회원_프로필_조회_테스트() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/members/{memberId}", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(TEST_MEMBER1_ID))
                .andExpect(jsonPath("$.imageUrl").value("http://test.com/test-profile.jpg"));
    }

    @Test
    void 다른_가족의_회원_프로필_조회_예외_검증() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/members/{memberId}", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER3_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("AU0006"));
    }

    @Test
    void 회원_프로필이미지_수정_테스트() throws Exception {
        // given
        UpdateMemberProfileImageUrlRequest request = new UpdateMemberProfileImageUrlRequest("https://bucket.com/new-image.jpg");

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/v1/members/profile-image-url/{memberId}", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(TEST_MEMBER1_ID))
                .andExpect(jsonPath("$.imageUrl").value(request.profileImageUrl()));
    }

    @Test
    void 타인의_프로필이미지_수정_예외_검증() throws Exception {
        // given
        UpdateMemberProfileImageUrlRequest request = new UpdateMemberProfileImageUrlRequest("https://bucket.com/new-image.jpg");

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/v1/members/profile-image-url/{memberId}", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER2_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("AU0006"));
    }

    @Test
    void 회원_프로필이미지_삭제_테스트() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/v1/members/profile-image-url/{memberId}", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    void 타인의_프로필이미지_삭제_예외_검증() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/v1/members/profile-image-url/{memberId}", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER2_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("AU0006"));
    }

    @Test
    void 회원_닉네임_수정_테스트() throws Exception {
        // given
        UpdateMemberNameRequest request = new UpdateMemberNameRequest("newName");

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/v1/members/name/{memberId}", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(TEST_MEMBER1_ID))
                .andExpect(jsonPath("$.name").value(request.name()));
    }

    @Test
    void 타인의_닉네임_수정_예외_검증() throws Exception {
        // given
        UpdateMemberNameRequest request = new UpdateMemberNameRequest("newName");

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/v1/members/name/{memberId}", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER3_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("AU0006"));
    }

    @Test
    void 회원탈퇴_이유없이_테스트() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/v1/members/{memberId}", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 회원탈퇴_이유있게_테스트() throws Exception {
        // given
        QuitMemberRequest quitMemberRequest = new QuitMemberRequest(List.of(MemberQuitReasonType.NO_FREQUENTLY_USE));

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/v1/members/{memberId}", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quitMemberRequest))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 회원탈퇴_이유여러개_테스트() throws Exception {
        // given
        QuitMemberRequest quitMemberRequest = new QuitMemberRequest(List.of(
                MemberQuitReasonType.NO_FREQUENTLY_USE, MemberQuitReasonType.SERVICE_UX_IS_BAD));

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/v1/members/{memberId}", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER1_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quitMemberRequest))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 타인의_탈퇴_예외_검증() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/v1/members/{memberId}", TEST_MEMBER1_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER2_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("AU0006"));
    }
}
