package com.oing.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.domain.Emoji;
import com.oing.domain.Member;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.CreateMyRealEmojiRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.UpdateMyRealEmojiRequest;
import com.oing.repository.MemberRealEmojiRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MemberRealEmojiApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private ObjectMapper objectMapper;

    private String TEST_MEMBER_ID = "01HGW2N7EHJVJ4CJ999RRS2E97";
    private String TEST_FAMILY_ID = "01HGW2N7EHJVJ4CJ999RRS2E44";
    private String TEST_MEMBER_REAL_EMOJI_ID = "01HGW2N7EHJVJ4CJ999RRS2A97";
    private String TEST_MEMBER_TOKEN;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberRealEmojiRepository memberRealEmojiRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(
                new Member(
                        TEST_MEMBER_ID,
                        TEST_FAMILY_ID,
                        LocalDate.now(),
                        "", "", "",
                        LocalDateTime.now()
                )
        );
        TEST_MEMBER_TOKEN = tokenGenerator
                .generateTokenPair(TEST_MEMBER_ID)
                .accessToken();
    }

    @Test
    void 리얼이모지_이미지_업로드_URL_요청_테스트() throws Exception {
        //given
        String imageName = "realEmoji.jpg";

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/v1/members/{memberId}/real-emoji/image-upload-request", TEST_MEMBER_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PreSignedUrlRequest(imageName)))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").exists());
    }

    @Test
    void 회원_리얼이모지_추가_테스트() throws Exception {
        //given
        String realEmojiImageUrl = "https://test.com/bucket/images/realEmoji.jpg";
        Emoji emoji = Emoji.EMOJI_1;
        CreateMyRealEmojiRequest request = new CreateMyRealEmojiRequest(emoji.getTypeKey(), realEmojiImageUrl);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/v1/members/{memberId}/real-emoji", TEST_MEMBER_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(emoji.getTypeKey()))
                .andExpect(jsonPath("$.imageUrl").value(realEmojiImageUrl));
    }

    @Test
    void 회원_리얼이모지_수정_테스트() throws Exception {
        //given
        String realEmojiImageUrl = "https://test.com/bucket/images/realEmoji.jpg";
        UpdateMyRealEmojiRequest request = new UpdateMyRealEmojiRequest(realEmojiImageUrl);
        memberRealEmojiRepository.save(
                new MemberRealEmoji(
                        TEST_MEMBER_REAL_EMOJI_ID,
                        TEST_MEMBER_ID,
                        TEST_FAMILY_ID,
                        Emoji.EMOJI_1,
                        "https://test.com/bucket/images/defaultEmoji.jpg",
                        "images/defaultEmoji.jpg"
                )
        );

        //when
        ResultActions resultActions = mockMvc.perform(
                put("/v1/members/{memberId}/real-emoji/{realEmojiId}", TEST_MEMBER_ID, TEST_MEMBER_REAL_EMOJI_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl").value(realEmojiImageUrl));
    }

    @Test
    void 회원_리얼이모지_조회_테스트() throws Exception {
        //given
        String realEmojiImageUrl = "https://test.com/bucket/images/realEmoji.jpg";
        memberRealEmojiRepository.save(
                new MemberRealEmoji(
                        TEST_MEMBER_REAL_EMOJI_ID,
                        TEST_MEMBER_ID,
                        TEST_FAMILY_ID,
                        Emoji.EMOJI_1,
                        realEmojiImageUrl,
                        "images/defaultEmoji.jpg"
                )
        );

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/members/{memberId}/real-emoji", TEST_MEMBER_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.myRealEmojiList[0].realEmojiId").value(TEST_MEMBER_REAL_EMOJI_ID))
                .andExpect(jsonPath("$.myRealEmojiList[0].type").value(Emoji.EMOJI_1.getTypeKey()))
                .andExpect(jsonPath("$.myRealEmojiList[0].imageUrl").value(realEmojiImageUrl));
    }
}
