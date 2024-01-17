package com.oing.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.domain.Member;
import com.oing.domain.MemberQuitReasonType;
import com.oing.dto.request.QuitMemberRequest;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MemberApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private ObjectMapper objectMapper;


    private String TEST_MEMBER_ID = "01HGW2N7EHJVJ4CJ999RRS2E97";
    private String TEST_MEMBER_TOKEN;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(
                new Member(
                        TEST_MEMBER_ID,
                        "testUser1",
                        LocalDate.now(),
                        "", "", "", ""
                )
        );
        TEST_MEMBER_TOKEN = tokenGenerator
                .generateTokenPair(TEST_MEMBER_ID)
                .accessToken();
    }

    @Test
    void 회원탈퇴_이유없이_테스트() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/v1/members/{memberId}", TEST_MEMBER_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
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
                delete("/v1/members/{memberId}", TEST_MEMBER_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
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
                delete("/v1/members/{memberId}", TEST_MEMBER_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quitMemberRequest))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
