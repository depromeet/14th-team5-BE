package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.Post;
import com.oing.domain.Reaction;
import com.oing.dto.request.PostReactionRequest;
import com.oing.dto.response.*;
import com.oing.exception.AuthorizationFailedException;
import com.oing.restapi.ReactionApi;
import com.oing.service.MemberBridge;
import com.oing.service.PostService;
import com.oing.service.ReactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReactionController implements ReactionApi {

    private final PostService postService;
    private final ReactionService reactionService;
    private final MemberBridge memberBridge;

    @Override
    public DefaultResponse createPostReaction(String postId, String loginMemberId, PostReactionRequest request) {
        log.info("Member {} is trying to create post reaction", loginMemberId);
        Post post = postService.getMemberPostById(postId);
        Reaction reaction = reactionService.createPostReaction(post, loginMemberId, request);

        log.info("Member {} has created post reaction {}", loginMemberId, reaction.getId());
        return DefaultResponse.ok();
    }

    @Override
    public DefaultResponse deletePostReaction(String postId, String loginMemberId, PostReactionRequest request) {
        log.info("Member {} is trying to delete post reaction {}", loginMemberId, request.content());
        Post post = postService.getMemberPostById(postId);
        reactionService.deletePostReaction(post, loginMemberId, request);

        return DefaultResponse.ok();
    }

    @Override
    public PostReactionSummaryResponse getPostReactionSummary(String postId, String loginMemberId) {
        Post post = postService.getMemberPostById(postId);
        validateFamilyMember(loginMemberId, post);

        List<PostReactionSummaryResponse.PostReactionSummaryResponseElement> results = post.getReactions()
                .stream()
                .collect(Collectors.groupingBy(Reaction::getEmoji))
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
    public ArrayResponse<PostReactionResponse> getPostReactions(String postId, String loginMemberId) {
        Post post = postService.getMemberPostById(postId);
        validateFamilyMember(loginMemberId, post);
        return ArrayResponse.of(
                post.getReactions().stream()
                        .map(PostReactionResponse::from)
                        .toList()
        );
    }

    @Override
    public PostReactionMemberResponse getPostReactionMembers(String postId, String loginMemberId) {
        Post post = postService.getMemberPostById(postId);
        validateFamilyMember(loginMemberId, post);
        List<Reaction> reactions = reactionService.getMemberPostReactionsByPostId(postId);
        List<Emoji> emojiList = Emoji.getEmojiList();

        Map<String, List<String>> emojiMemberIdsMap = reactions.stream()
                .collect(Collectors.groupingBy(
                        reaction -> reaction.getEmoji().getTypeKey(),
                        Collectors.mapping(Reaction::getMemberId, Collectors.toList())
                ));
        emojiList.forEach(emoji -> emojiMemberIdsMap.putIfAbsent(emoji.getTypeKey(), Collections.emptyList()));

        return new PostReactionMemberResponse(emojiMemberIdsMap);
    }

    private void validateFamilyMember(String memberId, Post post) {
        if (!memberBridge.isInSameFamily(memberId, post.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting reaction operation on post {}", memberId, post.getId());
            throw new AuthorizationFailedException();
        }
    }
}
