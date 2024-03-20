package com.oing.controller;


import com.oing.domain.Post;
import com.oing.domain.RealEmoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.dto.response.*;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.RealEmojiAlreadyExistsException;
import com.oing.exception.RegisteredRealEmojiNotFoundException;
import com.oing.restapi.RealEmojiApi;
import com.oing.service.MemberBridge;
import com.oing.service.RealEmojiService;
import com.oing.service.PostService;
import com.oing.service.MemberRealEmojiService;
import com.oing.util.IdentityGenerator;
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

    private final IdentityGenerator identityGenerator;
    private final PostService postService;
    private final RealEmojiService realEmojiService;
    private final MemberRealEmojiService memberRealEmojiService;
    private final MemberBridge memberBridge;

    /**
     * 게시물에 리얼 이모지를 등록합니다
     * @param postId 게시물 ID
     * @param request 리얼 이모지 등록 요청
     * @return 생성된 리얼 이모지
     * @throws AuthorizationFailedException 내 가족이 올린 게시물이 아닌 경우
     * @throws RealEmojiAlreadyExistsException 이미 등록된 리얼 이모지인 경우
     */
    @Transactional
    @Override
    public PostRealEmojiResponse createPostRealEmoji(String postId, String loginFamilyId, String loginMemberId, PostRealEmojiRequest request) {
        log.info("Member {} is trying to create post real emoji", loginMemberId);
        Post post = postService.getMemberPostById(postId);
        if (!memberBridge.isInSameFamily(loginMemberId, post.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting to access post real emoji", loginMemberId);
            throw new AuthorizationFailedException();
        }

        MemberRealEmoji realEmoji = memberRealEmojiService.getMemberRealEmojiByIdAndFamilyId(request.realEmojiId(), loginFamilyId);
        validatePostRealEmojiForAddition(post, loginMemberId, realEmoji);
        RealEmoji postRealEmoji = new RealEmoji(identityGenerator.generateIdentity(), realEmoji,
                post, loginMemberId);
        RealEmoji addedPostRealEmoji = realEmojiService.savePostRealEmoji(postRealEmoji);
        post.addRealEmoji(postRealEmoji);
        log.info("Member {} has created post real emoji {}", loginMemberId, realEmoji.getId());

        return PostRealEmojiResponse.from(addedPostRealEmoji);
    }

    private void validatePostRealEmojiForAddition(Post post, String memberId, MemberRealEmoji emoji) {
        if (realEmojiService.isMemberPostRealEmojiExists(post, memberId, emoji)) {
            throw new RealEmojiAlreadyExistsException();
        }
    }

    /**
     * 게시물에 등록된 리얼 이모지를 삭제합니다
     * @param postId 게시물 ID
     * @param realEmojiId 리얼 이모지 ID
     * @return 삭제 결과
     * @throws RegisteredRealEmojiNotFoundException 등록한 리얼 이모지가 없는 경우
     */
    @Transactional
    @Override
    public DefaultResponse deletePostRealEmoji(String postId, String realEmojiId, String loginMemberId) {
        log.info("Member {} is trying to delete post real emoji {}", loginMemberId, realEmojiId);
        Post post = postService.getMemberPostById(postId);
        RealEmoji postRealEmoji = realEmojiService
                .getMemberPostRealEmojiByRealEmojiIdAndMemberIdAndPostId(realEmojiId, loginMemberId, postId);

        realEmojiService.deletePostRealEmoji(postRealEmoji);
        post.removeRealEmoji(postRealEmoji);
        log.info("Member {} has deleted post real emoji {}", loginMemberId, realEmojiId);

        return DefaultResponse.ok();
    }

    /**
     * 게시물에 등록된 리얼 이모지 요약을 조회합니다
     * @param postId 게시물 ID
     * @return 리얼 이모지 요약
     */
    @Override
    @Transactional
    public PostRealEmojiSummaryResponse getPostRealEmojiSummary(String postId, String loginMemberId) {
        Post post = postService.findMemberPostById(postId);
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

    /**
     * 게시물에 등록된 리얼 이모지 목록을 조회합니다
     * @param postId 게시물 ID
     * @return 리얼 이모지 목록
     */
    @Transactional
    @Override
    public ArrayResponse<PostRealEmojiResponse> getPostRealEmojis(String postId, String loginMemberId) {
        Post post = postService.getMemberPostById(postId);
        validateFamilyMember(loginMemberId, post);
        return ArrayResponse.of(post.getRealEmojis().stream()
                .map(PostRealEmojiResponse::from)
                .toList()
        );
    }

    /**
     * 게시물에 등록된 리얼 이모지를 남긴 멤버 목록을 조회합니다
     * @param postId 게시물 ID
     * @return 리얼 이모지를 남긴 멤버 목록
     */
    @Transactional
    @Override
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

    /**
     * 리얼 이모지를 남긴 멤버 목록을 리얼 이모지 별로 그룹화합니다
     * @param realEmojis 리얼 이모지 목록
     * @return 리얼 이모지 별로 그룹화된 멤버 목록
     */
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
