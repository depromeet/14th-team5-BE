package com.oing.controller;


import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostRealEmoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.dto.response.*;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.RealEmojiAlreadyExistsException;
import com.oing.exception.RegisteredRealEmojiNotFoundException;
import com.oing.restapi.MemberPostRealEmojiApi;
import com.oing.service.MemberBridge;
import com.oing.service.MemberPostRealEmojiService;
import com.oing.service.MemberPostService;
import com.oing.service.MemberRealEmojiService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class MemberPostRealEmojiController implements MemberPostRealEmojiApi {

    private final AuthenticationHolder authenticationHolder;
    private final IdentityGenerator identityGenerator;
    private final MemberPostService memberPostService;
    private final MemberPostRealEmojiService memberPostRealEmojiService;
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
    public PostRealEmojiResponse createPostRealEmoji(String postId, PostRealEmojiRequest request) {
        String memberId = authenticationHolder.getUserId();
        MemberPost post = memberPostService.getMemberPostById(postId);
        if (!memberBridge.isInSameFamily(memberId, post.getMemberId()))
            throw new AuthorizationFailedException();

        MemberRealEmoji realEmoji = memberRealEmojiService.getMemberRealEmojiById(request.realEmojiId());
        validatePostRealEmojiForAddition(post, memberId, realEmoji);
        MemberPostRealEmoji postRealEmoji = new MemberPostRealEmoji(identityGenerator.generateIdentity(), realEmoji,
                post, memberId);
        MemberPostRealEmoji addedPostRealEmoji = memberPostRealEmojiService.savePostRealEmoji(postRealEmoji);
        post.addRealEmoji(postRealEmoji);
        return PostRealEmojiResponse.from(addedPostRealEmoji);
    }

    private void validatePostRealEmojiForAddition(MemberPost post, String memberId, MemberRealEmoji emoji) {
        if (memberPostRealEmojiService.isMemberPostRealEmojiExists(post, memberId, emoji)) {
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
    public DefaultResponse deletePostRealEmoji(String postId, String realEmojiId) {
        String memberId = authenticationHolder.getUserId();
        MemberPost post = memberPostService.getMemberPostById(postId);
        MemberPostRealEmoji postRealEmoji = memberPostRealEmojiService
                .getMemberPostRealEmojiByRealEmojiIdAndMemberId(realEmojiId, memberId);

        memberPostRealEmojiService.deletePostRealEmoji(postRealEmoji);
        post.removeRealEmoji(postRealEmoji);
        return DefaultResponse.ok();
    }

    /**
     * 게시물에 등록된 리얼 이모지 요약을 조회합니다
     * @param postId 게시물 ID
     * @return 리얼 이모지 요약
     */
    @Override
    @Transactional
    public PostRealEmojiSummaryResponse getPostRealEmojiSummary(String postId) {
        MemberPost post = memberPostService.findMemberPostById(postId);
        List<PostRealEmojiSummaryResponse.PostRealEmojiSummaryResponseElement> results = post.getRealEmojis()
                .stream()
                .collect(Collectors.groupingBy(MemberPostRealEmoji::getRealEmoji))
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
    public ArrayResponse<PostRealEmojiResponse> getPostRealEmojis(String postId) {
        MemberPost post = memberPostService.getMemberPostById(postId);
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
    public PostRealEmojiMemberResponse getPostRealEmojiMembers(String postId) {
        MemberPost post = memberPostService.getMemberPostById(postId);

        Map<MemberRealEmoji, List<String>> realEmojiMemberMap = groupByRealEmoji(post.getRealEmojis());
        Map<String, List<String>> result = realEmojiMemberMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getId(),
                        Map.Entry::getValue
                ));
        return new PostRealEmojiMemberResponse(result);
    }

    private Map<MemberRealEmoji, List<String>> groupByRealEmoji(List<MemberPostRealEmoji> realEmojis) {
        return realEmojis.stream()
                .collect(Collectors.groupingBy(
                        MemberPostRealEmoji::getRealEmoji,
                        Collectors.mapping(MemberPostRealEmoji::getMemberId, Collectors.toList())
                ));
    }
}
