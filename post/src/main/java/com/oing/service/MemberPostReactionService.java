package com.oing.service;

import com.oing.domain.Emoji;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostReaction;
import com.oing.exception.EmojiNotFoundException;
import com.oing.repository.MemberPostReactionRepository;
import com.oing.service.event.DeleteMemberPostEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberPostReactionService {

    private final MemberPostReactionRepository memberPostReactionRepository;

    public boolean isMemberPostReactionExists(MemberPost post, String memberId, Emoji emoji) {
        return memberPostReactionRepository.existsByPostAndMemberIdAndEmoji(post, memberId, emoji);
    }

    public MemberPostReaction createPostReaction(String reactionId, MemberPost post, String memberId, Emoji emoji) {
        MemberPostReaction reaction = new MemberPostReaction(reactionId, post, memberId, emoji);
        memberPostReactionRepository.save(reaction);
        return reaction;
    }

    public MemberPostReaction findReaction(MemberPost post, String memberId, Emoji emoji) {
        return memberPostReactionRepository
                .findReactionByPostAndMemberIdAndEmoji(post, memberId, emoji)
                .orElseThrow(EmojiNotFoundException::new);
    }

    public List<MemberPostReaction> getMemberPostReactionsByPostId(String postId) {
        return memberPostReactionRepository.findAllByPostId(postId);
    }

    public void deletePostReaction(MemberPostReaction reaction) {
        memberPostReactionRepository.deleteById(reaction.getId());
    }

    @EventListener
    public void deleteAllWhenPostDelete(DeleteMemberPostEvent event) {
        MemberPost post = event.memberPost();
        memberPostReactionRepository.deleteAllByPostId(post.getId());
    }
}
