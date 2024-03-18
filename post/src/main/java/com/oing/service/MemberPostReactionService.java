package com.oing.service;

import com.oing.domain.Emoji;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostReaction;
import com.oing.dto.request.PostReactionRequest;
import com.oing.exception.EmojiAlreadyExistsException;
import com.oing.exception.EmojiNotFoundException;
import com.oing.repository.MemberPostReactionRepository;
import com.oing.service.event.DeleteMemberPostEvent;
import com.oing.util.IdentityGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberPostReactionService {

    private final IdentityGenerator identityGenerator;
    private final MemberPostReactionRepository memberPostReactionRepository;
    private final MemberBridge memberBridge;

    public MemberPostReaction createPostReaction(MemberPost post, String memberId, PostReactionRequest request) {
        Emoji emoji = Emoji.fromString(request.content());
        validatePostReactionForAddition(post, memberId, emoji);
        String reactionId = identityGenerator.generateIdentity();

        MemberPostReaction reaction = new MemberPostReaction(reactionId, post, memberId, emoji);
        MemberPostReaction savedReaction = memberPostReactionRepository.save(reaction);
        post.addReaction(savedReaction);
        return savedReaction;
    }

    private void validatePostReactionForAddition(MemberPost post, String loginMemberId, Emoji emoji) {
        if (isMemberPostReactionExists(post, loginMemberId, emoji)) {
            throw new EmojiAlreadyExistsException();
        }
    }

    public List<MemberPostReaction> getMemberPostReactionsByPostId(String postId) {
        return memberPostReactionRepository.findAllByPostId(postId);
    }

    public void deletePostReaction(MemberPost post, String loginMemberId, PostReactionRequest request) {
        Emoji emoji = Emoji.fromString(request.content());

        validatePostReactionForDeletion(post, loginMemberId, emoji);
        MemberPostReaction reaction = findReaction(post, loginMemberId, emoji);
        post.removeReaction(reaction);
        memberPostReactionRepository.deleteById(reaction.getId());
        log.info("Member {} has deleted post reaction {}", loginMemberId, reaction.getId());
    }

    private void validatePostReactionForDeletion(MemberPost post, String memberId, Emoji emoji) {
        if (!isMemberPostReactionExists(post, memberId, emoji)) {
            throw new EmojiNotFoundException();
        }
    }

    public MemberPostReaction findReaction(MemberPost post, String memberId, Emoji emoji) {
        return memberPostReactionRepository
                .findReactionByPostAndMemberIdAndEmoji(post, memberId, emoji)
                .orElseThrow(EmojiNotFoundException::new);
    }

    public boolean isMemberPostReactionExists(MemberPost post, String memberId, Emoji emoji) {
        return memberPostReactionRepository.existsByPostAndMemberIdAndEmoji(post, memberId, emoji);
    }

    @EventListener
    public void deleteAllWhenPostDelete(DeleteMemberPostEvent event) {
        MemberPost post = event.memberPost();
        memberPostReactionRepository.deleteAllByPostId(post.getId());
    }
}
