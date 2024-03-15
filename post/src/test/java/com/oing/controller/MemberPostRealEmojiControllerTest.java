package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostRealEmoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.dto.response.ArrayResponse;
import com.oing.dto.response.PostRealEmojiMemberResponse;
import com.oing.dto.response.PostRealEmojiResponse;
import com.oing.dto.response.PostRealEmojiSummaryResponse;
import com.oing.exception.RegisteredRealEmojiNotFoundException;
import com.oing.service.MemberBridge;
import com.oing.service.MemberPostRealEmojiService;
import com.oing.service.MemberPostService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Transactional
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MemberPostRealEmojiControllerTest {
    @InjectMocks
    private MemberPostRealEmojiController memberPostRealEmojiController;

    @Mock
    private MemberPostService memberPostService;
    @Mock
    private MemberPostRealEmojiService memberPostRealEmojiService;
    @Mock
    private MemberBridge memberBridge;

    @Test
    void 게시물_리얼이모지_등록_테스트() {
        //given
        String memberId = "1";
        String familyId = "1";
        MemberRealEmoji realEmoji = new MemberRealEmoji("1", memberId, familyId,
                Emoji.EMOJI_1, "https://oing.com/emoji.jpg", "emoji.jpg");
        MemberPost post = new MemberPost("1", memberId, familyId, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        when(memberPostService.getMemberPostById(post.getId())).thenReturn(post);

        MemberPostRealEmoji postRealEmoji = new MemberPostRealEmoji("1", realEmoji, post, memberId);
        PostRealEmojiRequest request = new PostRealEmojiRequest(realEmoji.getId());
        when(memberPostRealEmojiService.registerRealEmojiAtPost(request, memberId, familyId, post)).thenReturn(postRealEmoji);

        //when
        PostRealEmojiResponse response = memberPostRealEmojiController.registerRealEmojiAtPost(post.getId(), familyId, memberId, request);

        //then
        assertEquals(post.getId(), response.postId());
        assertEquals(request.realEmojiId(), response.realEmojiId());
    }

    @Test
    void 게시물_리얼이모지_삭제_테스트() {
        //given
        String memberId = "1";
        String familyId = "1";
        MemberPost post = new MemberPost("1", memberId, familyId, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        MemberRealEmoji realEmoji = new MemberRealEmoji("1", memberId, familyId,
                Emoji.EMOJI_1, "https://oing.com/emoji.jpg", "emoji.jpg");
        when(memberPostService.getMemberPostById(post.getId())).thenReturn(post);

        //when
        memberPostRealEmojiController.deletePostRealEmoji(post.getId(), realEmoji.getId(), memberId);

        //then
        //nothing. just check no exception
    }

    @Test
    void 게시물_등록되지_않은_리얼이모지_삭제_예외_테스트() {
        //given
        String memberId = "1";
        String familyId = "1";
        MemberPost post = new MemberPost("1", memberId, familyId,"https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        MemberRealEmoji realEmoji = new MemberRealEmoji("1", memberId, familyId,
                Emoji.EMOJI_1, "https://oing.com/emoji.jpg", "emoji.jpg");
        when(memberPostService.getMemberPostById(post.getId())).thenReturn(post);

        //when
        when(memberPostRealEmojiService.getMemberPostRealEmojiByRealEmojiIdAndMemberIdAndPostId("1", memberId, post.getId()))
                .thenThrow(RegisteredRealEmojiNotFoundException.class);

        //then
        assertThrows(RegisteredRealEmojiNotFoundException.class,
                () -> memberPostRealEmojiController.deletePostRealEmoji(post.getId(), realEmoji.getId(), memberId));
    }

    @Test
    void 게시물_리얼이모지_요약_조회_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        when(memberPostService.getMemberPostById(post.getId())).thenReturn(post);
        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(true);

        //when
        PostRealEmojiSummaryResponse summary = memberPostRealEmojiController.getPostRealEmojiSummary(post.getId(), memberId);

        //then
        assertEquals(0, summary.results().size());
    }

    @Test
    void 게시물_리얼이모지_목록_조회_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        when(memberPostService.getMemberPostById(post.getId())).thenReturn(post);
        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(true);

        //when
        ArrayResponse<PostRealEmojiResponse> response = memberPostRealEmojiController.getPostRealEmojis(post.getId(), memberId);

        //then
        assertEquals(0, response.results().size());
        assertEquals(List.of(), response.results());
    }

    @Test
    void 게시물_리얼이모지_멤버_조회_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        when(memberPostService.getMemberPostById(post.getId())).thenReturn(post);
        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(true);

        //when
        PostRealEmojiMemberResponse response = memberPostRealEmojiController.getPostRealEmojiMembers(post.getId(), memberId);

        //then
        assertEquals(0, response.emojiMemberIdsList().size());
    }
}
