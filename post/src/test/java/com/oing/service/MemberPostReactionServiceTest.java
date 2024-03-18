package com.oing.service;

import com.oing.domain.Emoji;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostReaction;
import com.oing.dto.request.PostReactionRequest;
import com.oing.exception.EmojiAlreadyExistsException;
import com.oing.exception.EmojiNotFoundException;
import com.oing.repository.MemberPostReactionRepository;
import com.oing.util.IdentityGenerator;
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
public class MemberPostReactionServiceTest {

    @InjectMocks
    private MemberPostReactionService memberPostReactionService;

    @Mock
    private MemberPostReactionRepository memberPostReactionRepository;
    @Mock
    private IdentityGenerator identityGenerator;


    @Test
    void 게시물_리액션_생성_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1", "1");
        MemberPostReaction reaction = new MemberPostReaction("1", post, memberId, Emoji.EMOJI_1);

        //when
        PostReactionRequest request = new PostReactionRequest("emoji_1");
        when(memberPostReactionRepository.save(reaction)).thenReturn(reaction);
        when(identityGenerator.generateIdentity()).thenReturn("1");
        when(memberPostReactionService.isMemberPostReactionExists(post, memberId, Emoji.EMOJI_1)).thenReturn(false);
        MemberPostReaction savedReaction = memberPostReactionService.createPostReaction(post, memberId, request);

        //then
        assertEquals(reaction.getEmoji(), savedReaction.getEmoji());
    }

    @Test
    void 게시물_중복된_리액션_등록_예외_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1", "1");

        //when
        when(memberPostReactionService.isMemberPostReactionExists(post, memberId, Emoji.EMOJI_1)).thenReturn(true);
        PostReactionRequest request = new PostReactionRequest("emoji_1");

        //then
        assertThrows(EmojiAlreadyExistsException.class,
                () -> memberPostReactionService.createPostReaction(post, memberId, request));
    }

    @Test
    void 게시물_리액션_삭제_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1", "1");
        MemberPostReaction reaction = new MemberPostReaction("1", post, memberId, Emoji.EMOJI_1);
        when(memberPostReactionService.isMemberPostReactionExists(post, memberId, Emoji.EMOJI_1)).thenReturn(true);
        when(memberPostReactionRepository.findReactionByPostAndMemberIdAndEmoji(post, memberId, Emoji.EMOJI_1))
                .thenReturn(Optional.of(reaction));

        //when
        PostReactionRequest request = new PostReactionRequest("emoji_1");
        memberPostReactionService.deletePostReaction(post, memberId, request);

        //then
        //nothing. just check no exception
    }

    @Test
    void 게시물_존재하지_않는_리액션_삭제_예외_테스트() {
        //given
        String memberId = "1";
        MemberPost post = new MemberPost("1", memberId, "1", "1", "1", "1");
        when(memberPostReactionService.isMemberPostReactionExists(post, memberId, Emoji.EMOJI_1)).thenReturn(false);

        //when
        when(memberPostReactionService.isMemberPostReactionExists(post, memberId, Emoji.EMOJI_1)).thenReturn(false);
        PostReactionRequest request = new PostReactionRequest("emoji_1");

        //then
        assertThrows(EmojiNotFoundException.class,
                () -> memberPostReactionService.deletePostReaction(post, memberId, request));
    }
}
