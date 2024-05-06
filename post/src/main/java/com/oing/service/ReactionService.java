package com.oing.service;

import com.oing.domain.Emoji;
import com.oing.domain.Post;
import com.oing.domain.Reaction;
import com.oing.dto.request.PostReactionRequest;
import com.oing.exception.EmojiAlreadyExistsException;
import com.oing.exception.EmojiNotFoundException;
import com.oing.repository.ReactionRepository;
import com.oing.service.event.DeletePostEvent;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionService {

    private final IdentityGenerator identityGenerator;
    private final ReactionRepository reactionRepository;

    @Transactional
    public Reaction createPostReaction(Post post, String memberId, PostReactionRequest request) {
        Emoji emoji = Emoji.fromString(request.content());
        validatePostReactionForAddition(post, memberId, emoji);
        String reactionId = identityGenerator.generateIdentity();

        Reaction reaction = new Reaction(reactionId, post, memberId, emoji);
        Reaction savedReaction = reactionRepository.save(reaction);
        post.addReaction(savedReaction);
        return savedReaction;
    }

    private void validatePostReactionForAddition(Post post, String loginMemberId, Emoji emoji) {
        if (isMemberPostReactionExists(post, loginMemberId, emoji)) {
            throw new EmojiAlreadyExistsException();
        }
    }

    public List<Reaction> getMemberPostReactionsByPostId(String postId) {
        return reactionRepository.findAllByPostId(postId);
    }

    @Transactional
    public void deletePostReaction(Post post, String loginMemberId, PostReactionRequest request) {
        Emoji emoji = Emoji.fromString(request.content());

        validatePostReactionForDeletion(post, loginMemberId, emoji);
        Reaction reaction = findReaction(post, loginMemberId, emoji);
        post.removeReaction(reaction);
        reactionRepository.deleteById(reaction.getId());
        log.info("Member {} has deleted post reaction {}", loginMemberId, reaction.getId());
    }

    private void validatePostReactionForDeletion(Post post, String memberId, Emoji emoji) {
        if (!isMemberPostReactionExists(post, memberId, emoji)) {
            throw new EmojiNotFoundException();
        }
    }

    public Reaction findReaction(Post post, String memberId, Emoji emoji) {
        return reactionRepository
                .findReactionByPostAndMemberIdAndEmoji(post, memberId, emoji)
                .orElseThrow(EmojiNotFoundException::new);
    }

    public boolean isMemberPostReactionExists(Post post, String memberId, Emoji emoji) {
        return reactionRepository.existsByPostAndMemberIdAndEmoji(post, memberId, emoji);
    }

    public long countMonthlyReactionByMemberId(LocalDate date, String memberId) {
        int year = date.getYear();
        int month = date.getMonthValue();

        return reactionRepository.countMonthlyReactionByMemberId(year, month, memberId);
    }

    @EventListener
    public void deleteAllWhenPostDelete(DeletePostEvent event) {
        Post post = event.post();
        reactionRepository.deleteAllByPostId(post.getId());
    }
}
