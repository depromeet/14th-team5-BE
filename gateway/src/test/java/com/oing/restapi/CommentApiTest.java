package com.oing.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.domain.Comment;
import com.oing.domain.Member;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.repository.CommentRepository;
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
public class CommentApiTest {
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
    private CommentRepository commentRepository;

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
    void 게시물_댓글_추가_테스트() throws Exception {
        //given
        String comment = "testComment";
        CreatePostCommentRequest createPostCommentRequest = new CreatePostCommentRequest(
                comment
        );


        //when
        ResultActions resultActions = mockMvc.perform(
                post("/v1/posts/{postId}/comments", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPostCommentRequest))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value(comment))
                .andExpect(jsonPath("$.memberId").value(TEST_MEMBER_ID))
                .andExpect(jsonPath("$.postId").value(TEST_POST_ID));

    }

    @Test
    void 게시물_댓글_삭제_테스트() throws Exception {
        //given
        String commentId = "01HGW2N7EHJVJ4CJ999RRS2A97";
        commentRepository.save(
                new Comment(
                        commentId,
                        postRepository.getReferenceById(TEST_POST_ID),
                        TEST_MEMBER_ID,
                        "comment"
                )
        );

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/v1/posts/{postId}/comments/{commentId}", TEST_POST_ID, commentId)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 게시물_댓글_수정_테스트() throws Exception {
        //given
        String commentId = "01HGW2N7EHJVJ4CJ999RRS2A97";
        String newContent = "hello world";
        UpdatePostCommentRequest updatePostCommentRequest = new UpdatePostCommentRequest(
                newContent
        );
        commentRepository.save(
                new Comment(
                        commentId,
                        postRepository.getReferenceById(TEST_POST_ID),
                        TEST_MEMBER_ID,
                        "comment"
                )
        );

        //when
        ResultActions resultActions = mockMvc.perform(
                put("/v1/posts/{postId}/comments/{commentId}", TEST_POST_ID, commentId)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePostCommentRequest))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(commentId))
                .andExpect(jsonPath("$.comment").value(newContent))
                .andExpect(jsonPath("$.memberId").value(TEST_MEMBER_ID))
                .andExpect(jsonPath("$.postId").value(TEST_POST_ID))
        ;
    }

    @Test
    void 게시물_댓글_조회_테스트() throws Exception {
        //given
        String commentId = "01HGW2N7EHJVJ4CJ999RRS2A97";
        String content = "hello world";
        commentRepository.save(
                new Comment(
                        commentId,
                        postRepository.getReferenceById(TEST_POST_ID),
                        TEST_MEMBER_ID,
                        content
                )
        );

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/v1/posts/{postId}/comments", TEST_POST_ID)
                        .header("X-AUTH-TOKEN", TEST_MEMBER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].commentId").value(commentId))
                .andExpect(jsonPath("$.results[0].comment").value(content))
                .andExpect(jsonPath("$.results[0].memberId").value(TEST_MEMBER_ID))
                .andExpect(jsonPath("$.results[0].postId").value(TEST_POST_ID))
        ;
    }
}
