package com.oing.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.domain.Member;
import com.oing.domain.MemberPost;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.repository.MemberPostRepository;
import com.oing.repository.MemberRepository;
import com.oing.service.TokenGenerator;
import com.oing.support.EmbeddedRedisConfig;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(EmbeddedRedisConfig.class)
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MemberPostApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private ObjectMapper objectMapper;

    private String TEST_MEMBER_ID = "01HGW2N7EHJVJ4CJ999RRS2E97";
    private String TEST_POST_ID = "01HGW2N7EHJVJ4CJ999RRS2A97";
    private String TEST_MEMBER_TOKEN;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberPostRepository memberPostRepository;

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
    }

    @Test
    void 게시물_이미지_업로드_URL_요청_테스트() throws Exception {
        //given
        String imageName = "feed.jpg";

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/v1/posts/image-upload-request")
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
    void 게시물_추가_테스트() throws Exception {
        //given
        CreatePostRequest request = new CreatePostRequest("https://test.com/bucket/images/feed.jpg",
                "content", ZonedDateTime.now());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/v1/posts")
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorId").value(TEST_MEMBER_ID))
                .andExpect(jsonPath("$.imageUrl").value(request.imageUrl()))
                .andExpect(jsonPath("$.content").value(request.content()));
    }

    @Test
    void 게시물_삭제_테스트() throws Exception {
        //given
        memberPostRepository.save(new MemberPost(TEST_POST_ID, TEST_MEMBER_ID, "img", "img",
                "content"));

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/v1/posts/{postId}", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
