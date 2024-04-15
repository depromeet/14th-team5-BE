package com.oing.controller;

import com.oing.domain.*;
import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.dto.response.ArrayResponse;
import com.oing.dto.response.PostRealEmojiMemberResponse;
import com.oing.dto.response.PostRealEmojiResponse;
import com.oing.dto.response.PostRealEmojiSummaryResponse;
import com.oing.service.MemberBridge;
import com.oing.service.PostService;
import com.oing.service.RealEmojiService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Transactional
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class RealEmojiControllerTest {
    @InjectMocks
    private RealEmojiController realEmojiController;

    @Mock
    private PostService postService;
    @Mock
    private RealEmojiService realEmojiService;
    @Mock
    private MemberBridge memberBridge;

    @Test
    void 게시물_리얼이모지_등록_테스트() {
        //given
        String memberId = "1";
        String familyId = "1";
        MemberRealEmoji realEmoji = new MemberRealEmoji("1", memberId, familyId,
                Emoji.EMOJI_1, "https://oing.com/emoji.jpg", "emoji.jpg");
        Post post = new Post("1", memberId, familyId, Type.SURVIVAL, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        when(postService.getMemberPostById(post.getId())).thenReturn(post);

        RealEmoji postRealEmoji = new RealEmoji("1", realEmoji, post, memberId);
        PostRealEmojiRequest request = new PostRealEmojiRequest(realEmoji.getId());
        when(realEmojiService.registerRealEmojiAtPost(request, memberId, familyId, post)).thenReturn(postRealEmoji);

        //when
        PostRealEmojiResponse response = realEmojiController.registerRealEmojiAtPost(post.getId(), familyId, memberId, request);

        //then
        assertEquals(post.getId(), response.postId());
        assertEquals(request.realEmojiId(), response.realEmojiId());
    }

    @Test
    void 게시물_리얼이모지_삭제_테스트() {
        //given
        String memberId = "1";
        String familyId = "1";
        Post post = new Post("1", memberId, familyId, Type.SURVIVAL, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        MemberRealEmoji realEmoji = new MemberRealEmoji("1", memberId, familyId,
                Emoji.EMOJI_1, "https://oing.com/emoji.jpg", "emoji.jpg");
        when(postService.getMemberPostById(post.getId())).thenReturn(post);

        //when
        realEmojiController.deletePostRealEmoji(post.getId(), realEmoji.getId(), memberId);

        //then
        //nothing. just check no exception
    }

    @Test
    void 게시물_리얼이모지_요약_조회_테스트() {
        //given
        String memberId = "1";
        Post post = new Post("1", memberId, "1", Type.SURVIVAL, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        when(postService.getMemberPostById(post.getId())).thenReturn(post);
        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(true);

        //when
        PostRealEmojiSummaryResponse summary = realEmojiController.getPostRealEmojiSummary(post.getId(), memberId);

        //then
        assertEquals(0, summary.results().size());
    }

    @Test
    void 게시물_리얼이모지_목록_조회_테스트() {
        //given
        String memberId = "1";
        Post post = new Post("1", memberId, "1", Type.SURVIVAL, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        when(postService.getMemberPostById(post.getId())).thenReturn(post);
        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(true);

        //when
        ArrayResponse<PostRealEmojiResponse> response = realEmojiController.getPostRealEmojis(post.getId(), memberId);

        //then
        assertEquals(0, response.results().size());
        assertEquals(List.of(), response.results());
    }

    @Test
    void 게시물_리얼이모지_멤버_조회_테스트() {
        //given
        String memberId = "1";
        Post post = new Post("1", memberId, "1", Type.SURVIVAL, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        when(postService.getMemberPostById(post.getId())).thenReturn(post);
        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(true);

        //when
        PostRealEmojiMemberResponse response = realEmojiController.getPostRealEmojiMembers(post.getId(), memberId);

        //then
        assertEquals(0, response.emojiMemberIdsList().size());
    }
}
