package com.oing.service;

import com.oing.domain.Emoji;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostRealEmoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.RealEmojiAlreadyExistsException;
import com.oing.repository.MemberPostRealEmojiRepository;
import com.oing.repository.MemberRealEmojiRepository;
import com.oing.util.IdentityGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberPostRealEmojiServiceTest {

    @InjectMocks
    private MemberPostRealEmojiService memberPostRealEmojiService;

    @Mock
    private MemberPostRealEmojiRepository memberPostRealEmojiRepository;
    @Mock
    private MemberRealEmojiRepository memberRealEmojiRepository;
    @Mock
    private IdentityGenerator identityGenerator;
    @Mock
    private MemberBridge memberBridge;

    @Test
    void 게시물_리얼이모지_등록_테스트() {
        //given
        String memberId = "1";
        String familyId = "1";
        MemberPost post = new MemberPost("1", memberId, familyId, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        MemberRealEmoji memberRealEmoji = new MemberRealEmoji("1", memberId, familyId,
                Emoji.EMOJI_1, "https://oing.com/emoji.jpg", "emoji.jpg");
        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(true);
        when(memberRealEmojiRepository.findByIdAndFamilyId(memberRealEmoji.getId(), familyId)).thenReturn(Optional.of(memberRealEmoji));
        when(memberPostRealEmojiService.isMemberPostRealEmojiExists(post, memberId, memberRealEmoji)).thenReturn(false);

        //when
        MemberPostRealEmoji realEmoji = new MemberPostRealEmoji("1", memberRealEmoji, post, memberId);
        when(memberPostRealEmojiRepository.save(any())).thenReturn(realEmoji);
        PostRealEmojiRequest request = new PostRealEmojiRequest(realEmoji.getId());
        MemberPostRealEmoji result = memberPostRealEmojiService.registerRealEmojiAtPost(request, memberId, familyId, post);

        //then
        assertEquals(post.getId(), result.getPost().getId());
        assertEquals(request.realEmojiId(), result.getRealEmoji().getId());
    }

    @Test
    void 권한없는_memberId로_게시물_리얼이모지_등록_예외_테스트() {
        // given
        String memberId = "1";
        String familyId = "1";
        String otherFamilyId = "2";
        MemberPost post = new MemberPost("1", otherFamilyId, otherFamilyId, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        MemberRealEmoji memberRealEmoji = new MemberRealEmoji("1", memberId, familyId,
                Emoji.EMOJI_1, "https://oing.com/emoji.jpg", "emoji.jpg");
        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(false);

        //when
        PostRealEmojiRequest request = new PostRealEmojiRequest(memberRealEmoji.getId());

        // then
        assertThrows(AuthorizationFailedException.class,
                () -> memberPostRealEmojiService.registerRealEmojiAtPost(request, memberId, familyId, post));
    }

    @Test
    void 게시물_중복된_리얼이모지_등록_예외_테스트() {
        //given
        String memberId = "1";
        String familyId = "1";
        MemberPost post = new MemberPost("1", memberId, familyId, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        MemberRealEmoji memberRealEmoji = new MemberRealEmoji("1",  memberId, familyId, Emoji.EMOJI_1,
                "https://oing.com/emoji.jpg", "emoji.jpg");
        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(true);
        when(memberRealEmojiRepository.findByIdAndFamilyId(memberRealEmoji.getId(), familyId)).thenReturn(Optional.of(memberRealEmoji));
        when(memberPostRealEmojiRepository.existsByPostAndMemberIdAndRealEmoji(post, memberId, memberRealEmoji)).thenReturn(true);

        //when
        PostRealEmojiRequest request = new PostRealEmojiRequest(memberRealEmoji.getId());

        //then
        assertThrows(RealEmojiAlreadyExistsException.class,
                () -> memberPostRealEmojiService.registerRealEmojiAtPost(request, memberId, familyId, post));
    }
}
