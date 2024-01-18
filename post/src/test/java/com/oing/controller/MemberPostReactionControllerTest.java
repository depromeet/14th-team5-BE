package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostReaction;
import com.oing.dto.request.PostReactionRequest;
import com.oing.dto.response.PostReactionMemberResponse;
import com.oing.exception.EmojiAlreadyExistsException;
import com.oing.exception.EmojiNotFoundException;
import com.oing.service.MemberPostReactionService;
import com.oing.service.MemberPostService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberPostReactionControllerTest {
    @InjectMocks
    private MemberPostReactionController memberPostReactionController;

    @Mock
    private AuthenticationHolder authenticationHolder;
    @Mock
    private IdentityGenerator identityGenerator;
    @Mock
    private MemberPostService memberPostService;
    @Mock
    private MemberPostReactionService memberPostReactionService;

    @Test
    void 게시물_리액션_생성_테스트() {
        //given
        String memberId = "1";
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1");
        MemberPostReaction reaction = new MemberPostReaction("1", post, memberId, Emoji.EMOJI_1);
        when(memberPostService.findMemberPostById(post.getId())).thenReturn(post);
        when(memberPostReactionService.isMemberPostReactionExists(post, memberId, Emoji.EMOJI_1)).thenReturn(false);
        when(identityGenerator.generateIdentity()).thenReturn(reaction.getId());
        when(memberPostReactionService.createPostReaction(reaction.getId(), post, memberId, Emoji.EMOJI_1)).thenReturn(reaction);

        //when
        PostReactionRequest request = new PostReactionRequest("emoji_1");
        memberPostReactionController.createPostReaction(post.getId(), request);

        //then
        //nothing. just check no exception
    }

    @Test
    void 게시물_중복된_리액션_등록_예외_테스트() {
        //given
        String memberId = "1";
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1");
        when(memberPostService.findMemberPostById(post.getId())).thenReturn(post);

        //when
        when(memberPostReactionService.isMemberPostReactionExists(post, memberId, Emoji.EMOJI_1)).thenReturn(true);
        PostReactionRequest request = new PostReactionRequest("emoji_1");

        //then
        assertThrows(EmojiAlreadyExistsException.class,
                () -> memberPostReactionController.createPostReaction(post.getId(), request));
    }

    @Test
    void 게시물_리액션_삭제_테스트() {
        //given
        String memberId = "1";
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1");
        MemberPostReaction reaction = new MemberPostReaction("1", post, memberId, Emoji.EMOJI_1);
        when(memberPostService.findMemberPostById(post.getId())).thenReturn(post);
        when(memberPostReactionService.isMemberPostReactionExists(post, memberId, Emoji.EMOJI_1)).thenReturn(true);
        when(memberPostReactionService.findReaction(post, memberId, Emoji.EMOJI_1)).thenReturn(reaction);

        //when
        PostReactionRequest request = new PostReactionRequest("emoji_1");
        memberPostReactionController.deletePostReaction(post.getId(), request);

        //then
        //nothing. just check no exception
    }

    @Test
    void 게시물_존재하지_않는_리액션_삭제_예외_테스트() {
        //given
        String memberId = "1";
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1");
        when(memberPostService.findMemberPostById(post.getId())).thenReturn(post);

        //when
        when(memberPostReactionService.isMemberPostReactionExists(post, memberId, Emoji.EMOJI_1)).thenReturn(false);
        PostReactionRequest request = new PostReactionRequest("emoji_1");

        //then
        assertThrows(EmojiNotFoundException.class,
                () -> memberPostReactionController.deletePostReaction(post.getId(), request));
    }

    @Test
    void 리액션_남긴_멤버_조회_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1");
        List<MemberPostReaction> mockReactions = Arrays.asList(
                new MemberPostReaction("1", post, memberId, Emoji.EMOJI_1),
                new MemberPostReaction("2", post, memberId, Emoji.EMOJI_2)
        );
        when(memberPostReactionService.getMemberPostReactionsByPostId(post.getId())).thenReturn(mockReactions);

        //when
        PostReactionMemberResponse response = memberPostReactionController.getPostReactionMembers(post.getId());

        //then
        assertTrue(response.emojiMemberIdsList().get(Emoji.EMOJI_1.getTypeKey()).contains(memberId));
        assertTrue(response.emojiMemberIdsList().get(Emoji.EMOJI_2.getTypeKey()).contains(memberId));
        assertFalse(response.emojiMemberIdsList().get(Emoji.EMOJI_3.getTypeKey()).contains(memberId));
        assertFalse(response.emojiMemberIdsList().get(Emoji.EMOJI_4.getTypeKey()).contains(memberId));
        assertFalse(response.emojiMemberIdsList().get(Emoji.EMOJI_5.getTypeKey()).contains(memberId));
    }
}
