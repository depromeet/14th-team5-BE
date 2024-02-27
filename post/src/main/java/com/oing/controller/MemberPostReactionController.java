package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostReaction;
import com.oing.dto.request.PostReactionRequest;
import com.oing.dto.response.*;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.EmojiAlreadyExistsException;
import com.oing.exception.EmojiNotFoundException;
import com.oing.restapi.MemberPostReactionApi;
import com.oing.service.MemberBridge;
import com.oing.service.MemberPostReactionService;
import com.oing.service.MemberPostService;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
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
public class MemberPostReactionController implements MemberPostReactionApi {

    private final IdentityGenerator identityGenerator;
    private final MemberPostService memberPostService;
    private final MemberPostReactionService memberPostReactionService;
    private final MemberBridge memberBridge;

    @Override
    @Transactional
    public DefaultResponse createPostReaction(String postId, String loginMemberId, PostReactionRequest request) {
        log.info("Member {} is trying to create post reaction", loginMemberId);
        Emoji emoji = Emoji.fromString(request.content());
        MemberPost post = memberPostService.findMemberPostById(postId);

        validatePostReactionForAddition(post, loginMemberId, emoji);
        String reactionId = identityGenerator.generateIdentity();
        MemberPostReaction reaction = memberPostReactionService.createPostReaction(reactionId, post, loginMemberId, emoji);
        post.addReaction(reaction);
        log.info("Member {} has created post reaction {}", loginMemberId, reactionId);

        return DefaultResponse.ok();
    }

    private void validatePostReactionForAddition(MemberPost post, String loginMemberId, Emoji emoji) {
        if (memberPostReactionService.isMemberPostReactionExists(post, loginMemberId, emoji)) {
            throw new EmojiAlreadyExistsException();
        }
    }

    @Override
    @Transactional
    public DefaultResponse deletePostReaction(String postId, String loginMemberId, PostReactionRequest request) {
        log.info("Member {} is trying to delete post reaction {}", loginMemberId, request.content());
        Emoji emoji = Emoji.fromString(request.content());
        MemberPost post = memberPostService.findMemberPostById(postId);

        validatePostReactionForDeletion(post, loginMemberId, emoji);
        MemberPostReaction reaction = memberPostReactionService.findReaction(post, loginMemberId, emoji);
        post.removeReaction(reaction);
        memberPostReactionService.deletePostReaction(reaction);

        log.info("Member {} has deleted post reaction {}", loginMemberId, reaction.getId());
        return DefaultResponse.ok();
    }

    private void validatePostReactionForDeletion(MemberPost post, String memberId, Emoji emoji) {
        if (!memberPostReactionService.isMemberPostReactionExists(post, memberId, emoji)) {
            throw new EmojiNotFoundException();
        }
    }

    @Override
    @Transactional
    public PostReactionSummaryResponse getPostReactionSummary(String postId, String loginMemberId) {
        MemberPost post = memberPostService.findMemberPostById(postId);
        validateFamilyMember(loginMemberId, post);

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
    public ArrayResponse<PostReactionResponse> getPostReactions(String postId, String loginMemberId) {
        MemberPost post = memberPostService.findMemberPostById(postId);
        validateFamilyMember(loginMemberId, post);
        return ArrayResponse.of(
                post.getReactions().stream()
                        .map(PostReactionResponse::from)
                        .toList()
        );
    }

    @Override
    @Transactional
    public PostReactionMemberResponse getPostReactionMembers(String postId, String loginMemberId) {
        MemberPost post = memberPostService.findMemberPostById(postId);
        validateFamilyMember(loginMemberId, post);
        List<MemberPostReaction> reactions = memberPostReactionService.getMemberPostReactionsByPostId(postId);
        List<Emoji> emojiList = Emoji.getEmojiList();

        Map<String, List<String>> emojiMemberIdsMap = reactions.stream()
                .collect(Collectors.groupingBy(
                        reaction -> reaction.getEmoji().getTypeKey(),
                        Collectors.mapping(MemberPostReaction::getMemberId, Collectors.toList())
                ));
        emojiList.forEach(emoji -> emojiMemberIdsMap.putIfAbsent(emoji.getTypeKey(), Collections.emptyList()));

        return new PostReactionMemberResponse(emojiMemberIdsMap);
    }

    private void validateFamilyMember(String memberId, MemberPost post) {
        if (!memberBridge.isInSameFamily(memberId, post.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting reaction operation on post {}", memberId, post.getId());
            throw new AuthorizationFailedException();
        }
    }
}
