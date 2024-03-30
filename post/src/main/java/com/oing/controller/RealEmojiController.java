package com.oing.controller;


import com.oing.domain.MemberRealEmoji;
import com.oing.domain.Post;
import com.oing.domain.RealEmoji;
import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.dto.response.*;
import com.oing.exception.AuthorizationFailedException;
import com.oing.restapi.RealEmojiApi;
import com.oing.service.MemberBridge;
import com.oing.service.PostService;
import com.oing.service.RealEmojiService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RealEmojiController implements RealEmojiApi {

    private final PostService postService;
    private final RealEmojiService realEmojiService;
    private final MemberBridge memberBridge;


    @Override
    @Transactional
    public PostRealEmojiResponse registerRealEmojiAtPost(
            String postId, String loginFamilyId, String loginMemberId, PostRealEmojiRequest request
    ) {
        log.info("Member {} is trying to create post real emoji", loginMemberId);
        Post post = postService.getMemberPostById(postId);
        RealEmoji addedRealEmoji = realEmojiService.registerRealEmojiAtPost(request, loginMemberId,
                loginFamilyId, post);

        log.info("Member {} has created post real emoji {}", loginMemberId, addedRealEmoji.getId());

        return PostRealEmojiResponse.from(addedRealEmoji);
    }

    @Override
    @Transactional
    public DefaultResponse deletePostRealEmoji(String postId, String realEmojiId, String loginMemberId) {
        log.info("Member {} is trying to delete post real emoji {}", loginMemberId, realEmojiId);
        Post post = postService.getMemberPostById(postId);
        realEmojiService.deleteRealEmoji(loginMemberId, realEmojiId, post);

        return DefaultResponse.ok();
    }

    @Override
    @Transactional
    public PostRealEmojiSummaryResponse getPostRealEmojiSummary(String postId, String loginMemberId) {
        Post post = postService.getMemberPostById(postId);
        validateFamilyMember(loginMemberId, post);
        List<PostRealEmojiSummaryResponse.PostRealEmojiSummaryResponseElement> results = post.getRealEmojis()
                .stream()
                .collect(Collectors.groupingBy(RealEmoji::getRealEmoji))
                .values()
                .stream().map(element ->
                        new PostRealEmojiSummaryResponse.PostRealEmojiSummaryResponseElement(
                                element.get(0).getRealEmoji().getId(),
                                element.size()
                        )
                )
                .toList();
        return new PostRealEmojiSummaryResponse(
                post.getId(),
                results
        );
    }

    @Override
    @Transactional
    public ArrayResponse<PostRealEmojiResponse> getPostRealEmojis(String postId, String loginMemberId) {
        Post post = postService.getMemberPostById(postId);
        validateFamilyMember(loginMemberId, post);
        return ArrayResponse.of(post.getRealEmojis().stream()
                .map(PostRealEmojiResponse::from)
                .toList()
        );
    }

    @Override
    @Transactional
    public PostRealEmojiMemberResponse getPostRealEmojiMembers(String postId, String loginMemberId) {
        Post post = postService.getMemberPostById(postId);
        validateFamilyMember(loginMemberId, post);

        Map<MemberRealEmoji, List<String>> realEmojiMemberMap = groupByRealEmoji(post.getRealEmojis());
        Map<String, List<String>> result = realEmojiMemberMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getId(),
                        Map.Entry::getValue
                ));
        return new PostRealEmojiMemberResponse(result);
    }

    private Map<MemberRealEmoji, List<String>> groupByRealEmoji(List<RealEmoji> realEmojis) {
        return realEmojis.stream()
                .collect(Collectors.groupingBy(
                        RealEmoji::getRealEmoji,
                        Collectors.mapping(RealEmoji::getMemberId, Collectors.toList())
                ));
    }

    private void validateFamilyMember(String memberId, Post post) {
        if (!memberBridge.isInSameFamily(memberId, post.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting real emoji operation on post {}", memberId, post.getId());
            throw new AuthorizationFailedException();
        }
    }
}
