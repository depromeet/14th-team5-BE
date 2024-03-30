package com.oing.service;

import com.oing.domain.Emoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.CreateMyRealEmojiRequest;
import com.oing.dto.request.UpdateMyRealEmojiRequest;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.DuplicateRealEmojiException;
import com.oing.repository.MemberRealEmojiRepository;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberRealEmojiServiceTest {

    @InjectMocks
    private MemberRealEmojiService memberRealEmojiService;

    @Mock
    private MemberRealEmojiRepository memberRealEmojiRepository;
    @Mock
    private IdentityGenerator identityGenerator;
    @Mock
    private PreSignedUrlGenerator preSignedUrlGenerator;


    @Test
    void 회원_리얼이모지_생성_테스트() {
        // given
        String memberId = "1";
        String familyId = "1";
        String realEmojiImageUrl = "https://test.com/realEmoji.jpg";
        MemberRealEmoji realEmoji = new MemberRealEmoji("1", memberId, familyId, Emoji.EMOJI_1, realEmojiImageUrl, "realEmoji.jpg");
        Emoji emoji = Emoji.EMOJI_1;

        // when
        CreateMyRealEmojiRequest request = new CreateMyRealEmojiRequest(emoji.getTypeKey(), realEmojiImageUrl);
        when(memberRealEmojiRepository.save(realEmoji)).thenReturn(new MemberRealEmoji(
                "1", memberId, familyId, emoji, realEmojiImageUrl, "realEmoji.jpg"));
        when(identityGenerator.generateIdentity()).thenReturn("1");
        when(preSignedUrlGenerator.extractImageKey(realEmojiImageUrl)).thenReturn("realEmoji.jpg");
        MemberRealEmoji savedRealEmoji = memberRealEmojiService.save(memberId, memberId, familyId, request);

        // then
        assertEquals(emoji.getTypeKey(), savedRealEmoji.getType().getTypeKey());
        assertEquals(request.imageUrl(), savedRealEmoji.getRealEmojiImageUrl());
    }

    @Test
    void 권한없는_memberId로_리얼이모지_생성_예외_테스트() {
        // given
        String memberId = "1";
        String familyId = "1";
        String realEmojiImageUrl = "https://test.com/realEmoji.jpg";
        Emoji emoji = Emoji.EMOJI_1;

        // when
        CreateMyRealEmojiRequest request = new CreateMyRealEmojiRequest(emoji.getTypeKey(), realEmojiImageUrl);

        // then
        assertThrows(AuthorizationFailedException.class,
                () -> memberRealEmojiService.save(memberId, "2", familyId, request));
    }

    @Test
    void 중복된_리얼이모지_생성_예외_테스트() {
        // given
        String memberId = "1";
        String familyId = "1";
        String realEmojiImageUrl = "https://test.com/realEmoji.jpg";
        MemberRealEmoji realEmoji = new MemberRealEmoji("1", memberId, familyId, Emoji.EMOJI_1, realEmojiImageUrl, "realEmoji.jpg");
        Emoji emoji = Emoji.EMOJI_1;

        // when
        CreateMyRealEmojiRequest request = new CreateMyRealEmojiRequest(emoji.getTypeKey(), realEmojiImageUrl);
        when(memberRealEmojiRepository.findByTypeAndMemberIdAndFamilyId(emoji, memberId, familyId)).thenReturn(
                Optional.of(realEmoji)
        );

        // then
        assertThrows(DuplicateRealEmojiException.class,
                () -> memberRealEmojiService.save(memberId, memberId, familyId, request));
    }

    @Test
    void 회원_리얼이모지_수정_테스트() {
        // given
        String memberId = "1";
        String familyId = "1";
        String realEmojiId = "1";
        String realEmojiImageUrl = "https://test.com/realEmoji.jpg";
        String changedRealEmojiImageUrl = "https://test.com/changedRealEmoji.jpg";
        MemberRealEmoji realEmoji = new MemberRealEmoji(realEmojiId, memberId, familyId, Emoji.EMOJI_1, realEmojiImageUrl, "realEmoji.jpg");

        // when
        UpdateMyRealEmojiRequest request = new UpdateMyRealEmojiRequest(changedRealEmojiImageUrl);
        when(preSignedUrlGenerator.extractImageKey(changedRealEmojiImageUrl)).thenReturn("changedRealEmoji.jpg");
        when(memberRealEmojiRepository.findByIdAndFamilyId(realEmojiId, familyId)).thenReturn(Optional.of(realEmoji));
        MemberRealEmoji changedRealEmoji = memberRealEmojiService.changeMemberRealEmoji(memberId, memberId, familyId, realEmojiId, request);

        // then
        assertEquals(request.imageUrl(), changedRealEmoji.getRealEmojiImageUrl());
    }
}
