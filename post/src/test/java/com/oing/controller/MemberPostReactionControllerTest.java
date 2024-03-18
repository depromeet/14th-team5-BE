package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostReaction;
import com.oing.dto.request.PostReactionRequest;
import com.oing.dto.response.PostReactionMemberResponse;
import com.oing.service.MemberBridge;
import com.oing.service.MemberPostReactionService;
import com.oing.service.MemberPostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberPostReactionControllerTest {
    @InjectMocks
    private MemberPostReactionController memberPostReactionController;

    @Mock
    private MemberPostService memberPostService;
    @Mock
    private MemberPostReactionService memberPostReactionService;
    @Mock
    private MemberBridge memberBridge;

    @Test
    void 게시물_리액션_생성_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1", "1");
        MemberPostReaction reaction = new MemberPostReaction("1", post, memberId, Emoji.EMOJI_1);
        when(memberPostService.findMemberPostById(post.getId())).thenReturn(post);

        //when
        PostReactionRequest request = new PostReactionRequest("emoji_1");
        when(memberPostReactionService.createPostReaction(post, memberId, request)).thenReturn(reaction);
        memberPostReactionController.createPostReaction(post.getId(), memberId, request);

        //then
        //nothing. just check no exception
    }

    @Test
    void 게시물_리액션_삭제_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1", "1");
        when(memberPostService.findMemberPostById(post.getId())).thenReturn(post);

        //when
        PostReactionRequest request = new PostReactionRequest("emoji_1");
        memberPostReactionController.deletePostReaction(post.getId(), memberId, request);

        //then
        //nothing. just check no exception
    }

    @Test
    void 리액션_남긴_멤버_조회_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1", "1");
        List<MemberPostReaction> mockReactions = Arrays.asList(
                new MemberPostReaction("1", post, memberId, Emoji.EMOJI_1),
                new MemberPostReaction("2", post, memberId, Emoji.EMOJI_2)
        );
        when(memberPostService.findMemberPostById(post.getId())).thenReturn(post);
        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(true);
        when(memberPostReactionService.getMemberPostReactionsByPostId(post.getId())).thenReturn(mockReactions);

        //when
        PostReactionMemberResponse response = memberPostReactionController.getPostReactionMembers(post.getId(), memberId);

        //then
        assertTrue(response.emojiMemberIdsList().get(Emoji.EMOJI_1.getTypeKey()).contains(memberId));
        assertTrue(response.emojiMemberIdsList().get(Emoji.EMOJI_2.getTypeKey()).contains(memberId));
        assertFalse(response.emojiMemberIdsList().get(Emoji.EMOJI_3.getTypeKey()).contains(memberId));
        assertFalse(response.emojiMemberIdsList().get(Emoji.EMOJI_4.getTypeKey()).contains(memberId));
        assertFalse(response.emojiMemberIdsList().get(Emoji.EMOJI_5.getTypeKey()).contains(memberId));
    }
}
