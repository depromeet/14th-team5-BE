package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.model.MemberPost;
import com.oing.domain.model.MemberPostReaction;
import com.oing.dto.request.PostReactionRequest;
import com.oing.exception.EmojiAlreadyExistsException;
import com.oing.exception.EmojiNotFoundException;
import com.oing.restapi.PostReactionApi;
import com.oing.service.MemberPostService;
import com.oing.service.PostReactionService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PostReactionController implements PostReactionApi {

    private final AuthenticationHolder authenticationHolder;
    private final IdentityGenerator identityGenerator;
    private final MemberPostService memberPostService;
    private final PostReactionService postReactionService;

    @Override
    @Transactional
    public void createPostReaction(String postId, PostReactionRequest request) {
        String memberId = authenticationHolder.getUserId();
        Emoji emoji = Emoji.fromString(request.content());
        MemberPost post = memberPostService.findMemberPostById(postId);

        validatePostReactionForAddition(post, memberId, emoji);
        String reactionId = identityGenerator.generateIdentity();
        MemberPostReaction reaction = postReactionService.createPostReaction(reactionId, post, memberId, emoji);
        post.addReaction(reaction);
    }

    private void validatePostReactionForAddition(MemberPost post, String memberId, Emoji emoji) {
        if (postReactionService.isMemberPostReactionExists(post, memberId, emoji)) {
            throw new EmojiAlreadyExistsException();
        }
    }

    @Override
    @Transactional
    public void deletePostReaction(String postId, PostReactionRequest request) {
        String memberId = authenticationHolder.getUserId();
        Emoji emoji = Emoji.fromString(request.content());
        MemberPost post = memberPostService.findMemberPostById(postId);

        validatePostReactionForDeletion(post, memberId, emoji);
        MemberPostReaction reaction = postReactionService.findReaction(post, memberId, emoji);
        post.removeReaction(reaction);
        postReactionService.deletePostReaction(reaction);
    }

    private void validatePostReactionForDeletion(MemberPost post, String memberId, Emoji emoji) {
        if (!postReactionService.isMemberPostReactionExists(post, memberId, emoji)) {
            throw new EmojiNotFoundException();
        }
    }
}
