package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.CreateMyRealEmojiRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.UpdateMyRealEmojiRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.dto.response.RealEmojiResponse;
import com.oing.dto.response.RealEmojisResponse;
import com.oing.service.MemberRealEmojiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MemberRealEmojiControllerTest {
    @InjectMocks
    private MemberRealEmojiController memberRealEmojiController;

    @Mock
    private MemberRealEmojiService memberRealEmojiService;


    @Test
    void 리얼이모지_이미지_업로드_URL_요청_테스트() {
        // given
        String memberId = "1";
        String realEmojiImage = "realEmoji.jpg";

        // when
        PreSignedUrlRequest request = new PreSignedUrlRequest(realEmojiImage);
        PreSignedUrlResponse dummyResponse = new PreSignedUrlResponse("https://test.com/presigend-request-url.jpg");
        when(memberRealEmojiService.requestPresignedUrl(memberId, memberId, realEmojiImage)).thenReturn(dummyResponse);
        PreSignedUrlResponse response = memberRealEmojiController.requestPresignedUrl(memberId, memberId, request);

        // then
        assertNotNull(response.url());
    }

    @Test
    void 회원_리얼이모지_생성_테스트() {
        // given
        String memberId = "1";
        String familyId = "1";
        String realEmojiImageUrl = "https://test.com/realEmoji.jpg";
        Emoji emoji = Emoji.EMOJI_1;

        // when
        CreateMyRealEmojiRequest request = new CreateMyRealEmojiRequest(emoji.getTypeKey(), realEmojiImageUrl);
        when(memberRealEmojiService.save(any(), any(), any(), any())).thenReturn(new MemberRealEmoji("1", memberId, familyId, emoji,
                realEmojiImageUrl, "realEmoji.jpg"));
        RealEmojiResponse response = memberRealEmojiController.createMemberRealEmoji(memberId, memberId, familyId, request);

        // then
        assertEquals(emoji.getTypeKey(), response.type());
        assertEquals(request.imageUrl(), response.imageUrl());
    }

    @Test
    void 회원_리얼이모지_수정_테스트() {
        // given
        String memberId = "1";
        String familyId = "1";
        String realEmojiId = "1";
        String realEmojiImageUrl = "https://test.com/realEmoji.jpg";

        // when
        UpdateMyRealEmojiRequest request = new UpdateMyRealEmojiRequest(realEmojiImageUrl);
        when(memberRealEmojiService.changeMemberRealEmoji(memberId, memberId, familyId, realEmojiId, request)).thenReturn(
                new MemberRealEmoji("1", memberId, familyId, Emoji.EMOJI_1, realEmojiImageUrl, "realEmoji.jpg"));
        RealEmojiResponse response = memberRealEmojiController.changeMemberRealEmoji(memberId, memberId, familyId, realEmojiId, request);

        // then
        assertEquals(request.imageUrl(), response.imageUrl());
    }

    @Test
    void 회원_리얼이모지_조회_테스트() {
        // given
        String memberId = "1";
        String familyId = "1";
        String realEmojiImageUrl1 = "https://test.com/realEmoji1.jpg";
        String realEmojiImageUrl2 = "https://test.com/realEmoji2.jpg";
        Emoji emoji1 = Emoji.EMOJI_1;
        Emoji emoji2 = Emoji.EMOJI_4;
        CreateMyRealEmojiRequest request1 = new CreateMyRealEmojiRequest(emoji1.getTypeKey(), realEmojiImageUrl1);
        CreateMyRealEmojiRequest request2 = new CreateMyRealEmojiRequest(emoji1.getTypeKey(), realEmojiImageUrl2);
        when(memberRealEmojiService.save(any(), any(), any(), any())).thenReturn(new MemberRealEmoji("1", memberId, familyId, emoji1,
                realEmojiImageUrl1, "realEmoji1.jpg"));
        memberRealEmojiController.createMemberRealEmoji(memberId, memberId, familyId, request1);
        when(memberRealEmojiService.save(any(), any(), any(), any())).thenReturn(new MemberRealEmoji("2", memberId, familyId, emoji2,
                realEmojiImageUrl2, "realEmoji2.jpg"));
        memberRealEmojiController.createMemberRealEmoji(memberId, memberId, familyId, request2);

        // when
        when(memberRealEmojiService.findRealEmojisByMemberIdAndFamilyId(memberId, memberId, familyId)).thenReturn(List.of(
                new MemberRealEmoji("1", memberId, familyId, emoji1, realEmojiImageUrl1, "realEmoji1.jpg"),
                new MemberRealEmoji("2", memberId, familyId, emoji2, realEmojiImageUrl2, "realEmoji2.jpg")
        ));
        RealEmojisResponse response = memberRealEmojiController.getMemberRealEmojis(memberId, memberId, familyId);

        // then
        assertEquals(2, response.myRealEmojiList().size());
        assertEquals("1", response.myRealEmojiList().get(0).realEmojiId());
        assertEquals("2", response.myRealEmojiList().get(1).realEmojiId());
        assertEquals(emoji1.getTypeKey(), response.myRealEmojiList().get(0).type());
        assertEquals(emoji2.getTypeKey(), response.myRealEmojiList().get(1).type());
        assertEquals(request1.imageUrl(), response.myRealEmojiList().get(0).imageUrl());
        assertEquals(request2.imageUrl(), response.myRealEmojiList().get(1).imageUrl());
    }

    @Test
    void 다른_가족에서_생성된_회원_리얼이모지_조회_테스트() {
        // given
        String memberId = "1";
        String otherFamilyId = "1";
        String familyId = "2";
        String realEmojiImageUrl = "https://test.com/realEmoji1.jpg";
        Emoji emoji = Emoji.EMOJI_1;
        CreateMyRealEmojiRequest request = new CreateMyRealEmojiRequest(emoji.getTypeKey(), realEmojiImageUrl);
        when(memberRealEmojiService.save(any(), any(), any(), any())).thenReturn(new MemberRealEmoji("1", memberId, otherFamilyId, emoji,
                realEmojiImageUrl, "realEmoji.jpg"));
        memberRealEmojiController.createMemberRealEmoji(memberId, memberId, otherFamilyId, request);

        // when
        RealEmojisResponse response = memberRealEmojiController.getMemberRealEmojis(memberId, memberId, familyId);

        // then
        assertEquals(0, response.myRealEmojiList().size());
    }
}
