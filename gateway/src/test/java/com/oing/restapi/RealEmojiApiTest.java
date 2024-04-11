package com.oing.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.domain.*;
import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.repository.RealEmojiRepository;
import com.oing.repository.PostRepository;
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
public class RealEmojiApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private ObjectMapper objectMapper;

    private String TEST_MEMBER_ID = "01HGW2N7EHJVJ4CJ999RRS2E97";
    private String TEST_POST_ID = "01HGW2N7EHJVJ4CJ999RRS2A97";
    private String TEST_FAMILY_ID = "01HGW2N7EHJVJ4CJ999RRS2F97";
    private String TEST_MEMBER_REAL_EMOJI_ID = "01HGW2N7EHJVJ4CJ999RRS2A97";
    private String TEST_MEMBER_TOKEN;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRealEmojiRepository memberRealEmojiRepository;
    @Autowired
    private RealEmojiRepository realEmojiRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(new Member(TEST_MEMBER_ID, TEST_FAMILY_ID, LocalDate.now(), "",
                "", "",
                LocalDateTime.now()));
        TEST_MEMBER_TOKEN = tokenGenerator.generateTokenPair(TEST_MEMBER_ID).accessToken();

        postRepository.save(new Post(TEST_POST_ID, TEST_MEMBER_ID, TEST_FAMILY_ID, Type.FEED, "img", "img",
                "content"));

        memberRealEmojiRepository.save(new MemberRealEmoji(TEST_MEMBER_REAL_EMOJI_ID, TEST_MEMBER_ID, TEST_FAMILY_ID, Emoji.EMOJI_1,
                "https://test.com/bucket/real-emoji.jpg", "bucket/real-emoji.jpg"));
    }

    @Test
    void 게시물_리얼이모지_추가_테스트() throws Exception {
        //given
        PostRealEmojiRequest request = new PostRealEmojiRequest(TEST_MEMBER_REAL_EMOJI_ID);
        String emojiImageUrl = "https://test.com/bucket/real-emoji.jpg";

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/v1/posts/{postId}/real-emoji", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(TEST_POST_ID))
                .andExpect(jsonPath("$.memberId").value(TEST_MEMBER_ID))
                .andExpect(jsonPath("$.realEmojiId").value(TEST_MEMBER_REAL_EMOJI_ID))
                .andExpect(jsonPath("$.emojiImageUrl").value(emojiImageUrl));
    }

    @Test
    void 게시물_리얼이모지_삭제_테스트() throws Exception {
        //given
        MemberRealEmoji realEmoji = memberRealEmojiRepository.findById(TEST_MEMBER_REAL_EMOJI_ID).orElseThrow();
        Post post = postRepository.findById(TEST_POST_ID).orElseThrow();
        realEmojiRepository.save(new RealEmoji("1", realEmoji, post, TEST_MEMBER_ID));

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/v1/posts/{postId}/real-emoji/{realEmojiId}", TEST_POST_ID, TEST_MEMBER_REAL_EMOJI_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 게시물_리얼이모지_요약_조회_테스트() throws Exception {
        //given
        PostRealEmojiRequest request = new PostRealEmojiRequest(TEST_MEMBER_REAL_EMOJI_ID);
        mockMvc.perform(
                post("/v1/posts/{postId}/real-emoji", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/posts/{postId}/real-emoji/summary", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(TEST_POST_ID))
                .andExpect(jsonPath("$.results[0].realEmojiId").value(TEST_MEMBER_REAL_EMOJI_ID))
                .andExpect(jsonPath("$.results[0].count").value(1));
    }

    @Test
    void 게시물_리얼이모지_목록_조회_테스트() throws Exception {
        //given
        PostRealEmojiRequest request = new PostRealEmojiRequest(TEST_MEMBER_REAL_EMOJI_ID);
        mockMvc.perform(
                post("/v1/posts/{postId}/real-emoji", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/posts/{postId}/real-emoji", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].postId").value(TEST_POST_ID))
                .andExpect(jsonPath("$.results[0].memberId").value(TEST_MEMBER_ID))
                .andExpect(jsonPath("$.results[0].realEmojiId").value(TEST_MEMBER_REAL_EMOJI_ID))
                .andExpect(jsonPath("$.results[0].emojiImageUrl").value("https://test.com/bucket/real-emoji.jpg"));
    }

    @Test
    void 게시물_리얼이모지_남긴_멤버_조회_테스트() throws Exception {
        //given
        PostRealEmojiRequest request = new PostRealEmojiRequest(TEST_MEMBER_REAL_EMOJI_ID);
        mockMvc.perform(
                post("/v1/posts/{postId}/real-emoji", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/posts/{postId}/real-emoji/member", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emojiMemberIdsList['01HGW2N7EHJVJ4CJ999RRS2A97'][0]").value(TEST_MEMBER_ID));
    }
}
