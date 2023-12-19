package com.oing.service;

import com.oing.domain.Emoji;
import com.oing.domain.model.MemberPost;
import com.oing.domain.model.MemberPostReaction;
import com.oing.exception.EmojiNotFoundException;
import com.oing.repository.MemberPostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostReactionService {

    private final MemberPostReactionRepository memberPostReactionRepository;

    public boolean isMemberPostReactionExists(MemberPost post, String memberId, Emoji emoji) {
        return memberPostReactionRepository.existsByPostAndMemberIdAndEmoji(post, memberId, emoji);
    }

    public MemberPostReaction createPostReaction(String reactionId, MemberPost post, String memberId, Emoji emoji) {
        MemberPostReaction reaction = new MemberPostReaction(reactionId, post, memberId, emoji);
        memberPostReactionRepository.save(reaction);
        return reaction;
    }

    public MemberPostReaction findReaction(String postId, String memberId, Emoji emoji) {
        Optional<MemberPostReaction> reaction = memberPostReactionRepository
                .findReactionByPostIdAndMemberIdAndEmoji(postId, memberId, emoji);
        if (reaction.isPresent()) {
            return reaction.get();
        } else {
            throw new EmojiNotFoundException();
        }
    }

    public void deletePostReaction(MemberPostReaction reaction) {
        memberPostReactionRepository.deleteById(reaction.getId());
    }
}
