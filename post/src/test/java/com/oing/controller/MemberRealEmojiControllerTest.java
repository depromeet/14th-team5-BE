package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.CreateMyRealEmojiRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.UpdateMyRealEmojiRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.dto.response.RealEmojiResponse;
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
        when(memberRealEmojiService.save(any())).thenReturn(new MemberRealEmoji("1", memberId, Emoji.EMOJI_1,
                realEmojiImageUrl, "realEmoji.jpg"));
        RealEmojiResponse response = memberRealEmojiController.createMyRealEmoji(memberId, request);

        // then
        assertEquals(emoji.getTypeKey(), response.type());
        assertEquals(request.imageUrl(), response.imageUrl());
    }

    @Test
    void 이미_존재하는_리얼이모지_생성_예외_테스트() {
        // given
        String memberId = "1";
        String realEmojiImageUrl = "https://test.com/realEmoji.jpg";
        Emoji emoji = Emoji.EMOJI_1;

        // when
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        CreateMyRealEmojiRequest request = new CreateMyRealEmojiRequest(emoji.getTypeKey(), realEmojiImageUrl);
        when(memberRealEmojiService.save(any())).thenReturn(new MemberRealEmoji("1", memberId, Emoji.EMOJI_1,
                realEmojiImageUrl, "realEmoji.jpg"));
        when(memberRealEmojiService.save(any())).thenThrow(DuplicateRealEmojiException.class);

        // then
        assertThrows(DuplicateRealEmojiException.class,
                () -> memberRealEmojiController.createMyRealEmoji(memberId, request));
    }

    @Test
    void 회원_리얼이모지_변경_테스트() {
        // given
        String memberId = "1";
        String realEmojiId = "1";
        String realEmojiImageUrl = "https://test.com/realEmoji.jpg";

        // when
        when(authenticationHolder.getUserId()).thenReturn(memberId);
        UpdateMyRealEmojiRequest request = new UpdateMyRealEmojiRequest(realEmojiImageUrl);
        when(memberRealEmojiService.findRealEmojiById(realEmojiId)).thenReturn(new MemberRealEmoji("1", memberId,
                Emoji.EMOJI_1, realEmojiImageUrl, "realEmoji.jpg"));
        RealEmojiResponse response = memberRealEmojiController.changeMyRealEmoji(memberId, realEmojiId, request);

        // then
        assertEquals(request.imageUrl(), response.imageUrl());
    }
}