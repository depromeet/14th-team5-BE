package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.CreateMyRealEmojiRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.UpdateMyRealEmojiRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.dto.response.RealEmojiResponse;
import com.oing.dto.response.RealEmojisResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.DuplicateRealEmojiException;
import com.oing.service.MemberRealEmojiService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MemberRealEmojiControllerTest {
    @InjectMocks
    private MemberRealEmojiController memberRealEmojiController;

    @Mock
    private AuthenticationHolder authenticationHolder;
    @Mock
    private IdentityGenerator identityGenerator;
    @Mock
    private MemberRealEmojiService memberRealEmojiService;
    @Mock
    private PreSignedUrlGenerator preSignedUrlGenerator;

    @Test
    void 리얼이모지_이미지_업로드_URL_요청_테스트() {
        // given
        String memberId = "1";
        String realEmojiImage = "realEmoji.jpg";

        // when
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        PreSignedUrlRequest request = new PreSignedUrlRequest(realEmojiImage);
        PreSignedUrlResponse dummyResponse = new PreSignedUrlResponse("https://test.com/presigend-request-url");
        when(preSignedUrlGenerator.getRealEmojiPreSignedUrl(any())).thenReturn(dummyResponse);
        PreSignedUrlResponse response = memberRealEmojiController.requestPresignedUrl(memberId, request);

        // then
        assertNotNull(response.url());
    }

    @Test
    void 회원_리얼이모지_생성_테스트() {
        // given
        String memberId = "1";
        String realEmojiImageUrl = "https://test.com/realEmoji.jpg";
        Emoji emoji = Emoji.EMOJI_1;

        // when
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        CreateMyRealEmojiRequest request = new CreateMyRealEmojiRequest(emoji.getTypeKey(), realEmojiImageUrl);
        when(memberRealEmojiService.save(any())).thenReturn(new MemberRealEmoji("1", memberId, emoji,
                realEmojiImageUrl, "realEmoji.jpg"));
        RealEmojiResponse response = memberRealEmojiController.createMemberRealEmoji(memberId, request);

        // then
        assertEquals(emoji.getTypeKey(), response.type());
        assertEquals(request.imageUrl(), response.imageUrl());
    }

    @Test
    void 권한없는_memberId로_리얼이모지_생성_예외_테스트() {
        // given
        String memberId = "1";
        String realEmojiImageUrl = "https://test.com/realEmoji.jpg";
        Emoji emoji = Emoji.EMOJI_1;

        // when
        when(authenticationHolder.getUserId()).thenReturn("2");
        CreateMyRealEmojiRequest request = new CreateMyRealEmojiRequest(emoji.getTypeKey(), realEmojiImageUrl);

        // then
        assertThrows(AuthorizationFailedException.class,
                () -> memberRealEmojiController.createMemberRealEmoji(memberId, request));
    }

    @Test
    void 중복된_리얼이모지_생성_예외_테스트() {
        // given
        String memberId = "1";
        String realEmojiImageUrl = "https://test.com/realEmoji.jpg";
        Emoji emoji = Emoji.EMOJI_1;

        // when
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        CreateMyRealEmojiRequest request = new CreateMyRealEmojiRequest(emoji.getTypeKey(), realEmojiImageUrl);
        when(memberRealEmojiService.findRealEmojiByEmojiType(emoji)).thenReturn(true);

        // then
        assertThrows(DuplicateRealEmojiException.class,
                () -> memberRealEmojiController.createMemberRealEmoji(memberId, request));
    }

    @Test
    void 회원_리얼이모지_수정_테스트() {
        // given
        String memberId = "1";
        String realEmojiId = "1";
        String realEmojiImageUrl = "https://test.com/realEmoji.jpg";

        // when
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        UpdateMyRealEmojiRequest request = new UpdateMyRealEmojiRequest(realEmojiImageUrl);
        when(memberRealEmojiService.findRealEmojiById(realEmojiId)).thenReturn(new MemberRealEmoji("1", memberId,
                Emoji.EMOJI_1, realEmojiImageUrl, "realEmoji.jpg"));
        RealEmojiResponse response = memberRealEmojiController.changeMemberRealEmoji(memberId, realEmojiId, request);

        // then
        assertEquals(request.imageUrl(), response.imageUrl());
    }

    @Test
    void 회원_리얼이모지_조회_테스트() {
        // given
        String memberId = "1";
        String realEmojiImageUrl1 = "https://test.com/realEmoji1.jpg";
        String realEmojiImageUrl2 = "https://test.com/realEmoji2.jpg";
        Emoji emoji1 = Emoji.EMOJI_1;
        Emoji emoji2 = Emoji.EMOJI_4;
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        CreateMyRealEmojiRequest request1 = new CreateMyRealEmojiRequest(emoji1.getTypeKey(), realEmojiImageUrl1);
        CreateMyRealEmojiRequest request2 = new CreateMyRealEmojiRequest(emoji1.getTypeKey(), realEmojiImageUrl2);
        when(memberRealEmojiService.save(any())).thenReturn(new MemberRealEmoji("1", memberId, emoji1,
                realEmojiImageUrl1, "realEmoji1.jpg"));
        memberRealEmojiController.createMemberRealEmoji(memberId, request1);
        when(memberRealEmojiService.save(any())).thenReturn(new MemberRealEmoji("2", memberId, emoji2,
                realEmojiImageUrl2, "realEmoji2.jpg"));
        memberRealEmojiController.createMemberRealEmoji(memberId, request2);

        // when
        when(memberRealEmojiService.findRealEmojisByMemberId(memberId)).thenReturn(List.of(
                new MemberRealEmoji("1", memberId, emoji1, realEmojiImageUrl1, "realEmoji1.jpg"),
                new MemberRealEmoji("2", memberId, emoji2, realEmojiImageUrl2, "realEmoji2.jpg")
        ));
        RealEmojisResponse response = memberRealEmojiController.getMemberRealEmojis(memberId);

        // then
        assertEquals(2, response.myRealEmojiList().size());
        assertEquals("1", response.myRealEmojiList().get(0).realEmojiId());
        assertEquals("2", response.myRealEmojiList().get(1).realEmojiId());
        assertEquals(emoji1.getTypeKey(), response.myRealEmojiList().get(0).type());
        assertEquals(emoji2.getTypeKey(), response.myRealEmojiList().get(1).type());
        assertEquals(request1.imageUrl(), response.myRealEmojiList().get(0).imageUrl());
        assertEquals(request2.imageUrl(), response.myRealEmojiList().get(1).imageUrl());
    }
}
