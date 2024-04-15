package com.oing.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.domain.*;
import com.oing.dto.request.PostReactionRequest;
import com.oing.repository.ReactionRepository;
import com.oing.repository.PostRepository;
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
public class ReactionApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private ObjectMapper objectMapper;

    private String TEST_MEMBER_ID = "01HGW2N7EHJVJ4CJ999RRS2E97";
    private String TEST_POST_ID = "01HGW2N7EHJVJ4CJ999RRS2A97";
    private String TEST_FAMILY_ID = "01HGW2N7EHJVJ4CJ999RRS2E44";
    private String TEST_MEMBER_TOKEN;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReactionRepository reactionRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(
                new Member(
                        TEST_MEMBER_ID,
                        "testUser1",
                        LocalDate.now(),
                        "", "", "",
                        LocalDateTime.now()
                )
        );
        TEST_MEMBER_TOKEN = tokenGenerator
                .generateTokenPair(TEST_MEMBER_ID)
                .accessToken();
        postRepository.save(
                new Post(
                        TEST_POST_ID,
                        TEST_MEMBER_ID,
                        TEST_FAMILY_ID,
                        PostType.SURVIVAL,
                        "img",
                        "img",
                        "content"
                )
        );
    }

    @Test
    void 게시물_리액션_추가_테스트() throws Exception {
        //given
        Emoji emoji = Emoji.EMOJI_1;
        PostReactionRequest request = new PostReactionRequest(emoji.getTypeKey());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/v1/posts/{postId}/reactions", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 게시물_리액션_삭제_테스트() throws Exception {
        //given
        Emoji emoji = Emoji.EMOJI_1;
        PostReactionRequest request = new PostReactionRequest(emoji.getTypeKey());
        reactionRepository.save(new Reaction("1", postRepository.getReferenceById(TEST_POST_ID),
                TEST_MEMBER_ID, emoji));

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/v1/posts/{postId}/reactions", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 게시물_리액션_남긴_멤버_조회() throws Exception {
        //given
        Emoji emoji = Emoji.EMOJI_3;
        reactionRepository.save(new Reaction("1", postRepository.getReferenceById(TEST_POST_ID),
                TEST_MEMBER_ID, emoji));

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/posts/{postId}/reactions/member", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emojiMemberIdsList.emoji_3[0]").value(TEST_MEMBER_ID));
    }
}
