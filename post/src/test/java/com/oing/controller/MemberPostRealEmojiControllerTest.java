package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostRealEmoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.dto.response.PostRealEmojiResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.RealEmojiAlreadyExistsException;
import com.oing.exception.RegisteredRealEmojiNotFoundException;
import com.oing.service.MemberBridge;
import com.oing.service.MemberPostRealEmojiService;
import com.oing.service.MemberPostService;
import com.oing.service.MemberRealEmojiService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberPostRealEmojiControllerTest {
    @InjectMocks
    private MemberPostRealEmojiController memberPostRealEmojiController;

    @Mock
    private AuthenticationHolder authenticationHolder;
    @Mock
    private IdentityGenerator identityGenerator;
    @Mock
    private MemberPostService memberPostService;
    @Mock
    private MemberPostRealEmojiService memberPostRealEmojiService;
    @Mock
    private MemberRealEmojiService memberRealEmojiService;
    @Mock
    private MemberBridge memberBridge;

    @Test
    void 게시물_리얼이모지_등록_테스트() {
        //given
        String memberId = "1";
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        when(memberBridge.isInSameFamily(memberId, memberId)).thenReturn(true);
        MemberPost post = new MemberPost("1", memberId, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        MemberRealEmoji realEmoji = new MemberRealEmoji("1", memberId,
                Emoji.EMOJI_1, "https://oing.com/emoji.jpg", "emoji.jpg");
        when(memberPostService.getMemberPostById(post.getId())).thenReturn(post);
        when(memberRealEmojiService.getMemberRealEmojiById(realEmoji.getId())).thenReturn(realEmoji);

        MemberPostRealEmoji postRealEmoji = new MemberPostRealEmoji("1", realEmoji, post, memberId);
        when(memberPostRealEmojiService.savePostRealEmoji(any(MemberPostRealEmoji.class))).thenReturn(postRealEmoji);
        when(identityGenerator.generateIdentity()).thenReturn(postRealEmoji.getId());
        PostRealEmojiRequest request = new PostRealEmojiRequest(realEmoji.getId());

        //when
        PostRealEmojiResponse response = memberPostRealEmojiController.createPostRealEmoji(post.getId(), request);

        //then
        assertEquals(post.getId(), response.postId());
        assertEquals(request.realEmojiId(), response.realEmojiId());
    }

    @Test
    void 권한없는_memberId로_게시물_리얼이모지_등록_예외_테스트() {
        // given
        String memberId = "1";
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        MemberPost post = new MemberPost("1", memberId, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        MemberRealEmoji realEmoji = new MemberRealEmoji("1", memberId,
                Emoji.EMOJI_1, "https://oing.com/emoji.jpg", "emoji.jpg");
        when(memberPostService.getMemberPostById(post.getId())).thenReturn(post);

        //when
        when(memberBridge.isInSameFamily(memberId, memberId)).thenReturn(false);
        PostRealEmojiRequest request = new PostRealEmojiRequest(realEmoji.getId());

        // then
        assertThrows(AuthorizationFailedException.class,
                () -> memberPostRealEmojiController.createPostRealEmoji(post.getId(), request));
    }

    @Test
    void 게시물_중복된_리얼이모지_등록_예외_테스트() {
        //given
        String memberId = "1";
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        when(memberBridge.isInSameFamily(memberId, memberId)).thenReturn(true);
        MemberPost post = new MemberPost("1", memberId, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        MemberRealEmoji realEmoji = new MemberRealEmoji("1",  memberId, Emoji.EMOJI_1,
                "https://oing.com/emoji.jpg", "emoji.jpg");
        when(memberPostService.getMemberPostById(post.getId())).thenReturn(post);
        when(memberRealEmojiService.getMemberRealEmojiById(realEmoji.getId())).thenReturn(realEmoji);

        //when
        when(memberPostRealEmojiService.isMemberPostRealEmojiExists(post, memberId, realEmoji)).thenReturn(true);
        PostRealEmojiRequest request = new PostRealEmojiRequest(realEmoji.getId());

        //then
        assertThrows(RealEmojiAlreadyExistsException.class,
                () -> memberPostRealEmojiController.createPostRealEmoji(post.getId(), request));
    }

    @Test
    void 게시물_리얼이모지_삭제_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        MemberRealEmoji realEmoji = new MemberRealEmoji("1", memberId,
                Emoji.EMOJI_1, "https://oing.com/emoji.jpg", "emoji.jpg");
        when(memberPostService.getMemberPostById(post.getId())).thenReturn(post);

        //when
        memberPostRealEmojiController.deletePostRealEmoji(post.getId(), realEmoji.getId());

        //then
        //nothing. just check no exception
    }

    @Test
    void 게시물_등록되지_않은_리얼이모지_삭제_예외_테스트() {
        //given
        String memberId = "1";
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        MemberPost post = new MemberPost("1", memberId, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        MemberRealEmoji realEmoji = new MemberRealEmoji("1", memberId,
                Emoji.EMOJI_1, "https://oing.com/emoji.jpg", "emoji.jpg");
        when(memberPostService.getMemberPostById(post.getId())).thenReturn(post);

        //when
        when(memberPostRealEmojiService.getMemberPostRealEmojiByRealEmojiIdAndMemberId("1", memberId))
                .thenThrow(RegisteredRealEmojiNotFoundException.class);

        //then
        assertThrows(RegisteredRealEmojiNotFoundException.class,
                () -> memberPostRealEmojiController.deletePostRealEmoji(post.getId(), realEmoji.getId()));
    }
}