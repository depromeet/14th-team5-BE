package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.model.MemberPost;
import com.oing.domain.model.MemberPostReaction;
import com.oing.dto.request.PostReactionRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PostReactionSummaryResponse;
import com.oing.dto.response.PostReactionsResponse;
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

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostReactionController implements PostReactionApi {

    private final AuthenticationHolder authenticationHolder;
    private final IdentityGenerator identityGenerator;
    private final MemberPostService memberPostService;
    private final PostReactionService postReactionService;

    @Override
    @Transactional
    public DefaultResponse createPostReaction(String postId, PostReactionRequest request) {
        String memberId = authenticationHolder.getUserId();
        Emoji emoji = Emoji.fromString(request.content());
        MemberPost post = memberPostService.findMemberPostById(postId);

        validatePostReactionForAddition(post, memberId, emoji);
        String reactionId = identityGenerator.generateIdentity();
        MemberPostReaction reaction = postReactionService.createPostReaction(reactionId, post, memberId, emoji);
        post.addReaction(reaction);

        return DefaultResponse.ok();
    }

    private void validatePostReactionForAddition(MemberPost post, String memberId, Emoji emoji) {
        if (postReactionService.isMemberPostReactionExists(post, memberId, emoji)) {
            throw new EmojiAlreadyExistsException();
        }
    }

    @Override
    @Transactional
    public DefaultResponse deletePostReaction(String postId, PostReactionRequest request) {
        String memberId = authenticationHolder.getUserId();
        Emoji emoji = Emoji.fromString(request.content());
        MemberPost post = memberPostService.findMemberPostById(postId);

        validatePostReactionForDeletion(post, memberId, emoji);
        MemberPostReaction reaction = postReactionService.findReaction(post, memberId, emoji);
        post.removeReaction(reaction);
        postReactionService.deletePostReaction(reaction);

        return DefaultResponse.ok();
    }

    @Override
    @Transactional
    public PostReactionSummaryResponse getPostReactionSummary(String postId) {
        MemberPost post = memberPostService.findMemberPostById(postId);
        List<PostReactionSummaryResponse.PostReactionSummaryResponseElement> results = post.getReactions()
                .stream()
                .collect(Collectors.groupingBy(MemberPostReaction::getEmoji))
                .values()
                .stream().map(element ->
                        new PostReactionSummaryResponse.PostReactionSummaryResponseElement(
                                element.get(0).getEmoji().getTypeKey(),
                                element.size()
                        )
                )
                .toList();
        return new PostReactionSummaryResponse(
                post.getId(),
                results
        );
    }

    @Override
    @Transactional
    public PostReactionsResponse getPostReactions(String postId) {
        List<MemberPostReaction> reactions = postReactionService.getMemberPostReactionsByPostId(postId);
        List<Emoji> emojiList = Emoji.getEmojiList();

        Map<String, List<String>> emojiMemberIdsMap = reactions.stream()
                .collect(Collectors.groupingBy(
                        reaction -> reaction.getEmoji().getTypeKey(),
                        Collectors.mapping(MemberPostReaction::getMemberId, Collectors.toList())
                ));
        emojiList.forEach(emoji -> emojiMemberIdsMap.putIfAbsent(emoji.getTypeKey(), Collections.emptyList()));

        return new PostReactionsResponse(emojiMemberIdsMap);
    }

    private void validatePostReactionForDeletion(MemberPost post, String memberId, Emoji emoji) {
        if (!postReactionService.isMemberPostReactionExists(post, memberId, emoji)) {
            throw new EmojiNotFoundException();
        }
    }
}
