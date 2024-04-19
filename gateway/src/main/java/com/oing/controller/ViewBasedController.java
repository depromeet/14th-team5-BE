package com.oing.controller;

import com.oing.domain.*;
import com.oing.dto.response.FamilyMemberProfileResponse;
import com.oing.dto.response.MainPageFeedResponse;
import com.oing.dto.response.MainPageResponse;
import com.oing.dto.response.MainPageTopBarResponse;
import com.oing.restapi.ViewBasedApi;
import com.oing.service.MemberBridge;
import com.oing.service.MemberPickService;
import com.oing.service.MemberService;
import com.oing.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/16/24
 * Time: 4:03 PM
 */
@Controller
@RequiredArgsConstructor
public class ViewBasedController implements ViewBasedApi {
    private final PostService postService;
    private final MemberService memberService;
    private final MemberPickService memberPickService;
    private final MemberBridge memberBridge;

    private static final int PAGE_FETCH_SIZE = 1000;
    @Override
    public MainPageResponse getMainPage(
            String loginMemberId
    ) {
        String familyId = memberBridge.getFamilyIdByMemberId(loginMemberId);
        LocalDate today = LocalDate.now();
        Map<String, FamilyMemberProfileResponse> memberMap = new HashMap<>();
        Page<FamilyMemberProfileResponse> members = memberService.getFamilyMembersProfilesByFamilyId(familyId, 1, PAGE_FETCH_SIZE);
        members.forEach(member -> memberMap.put(member.memberId(), member));
        Collection<Post> survivalPosts = postService.searchMemberPost(
                1, PAGE_FETCH_SIZE, today, null, loginMemberId, familyId,
                true, PostType.SURVIVAL
        ).results();
        Collection<Post> missionPosts = postService.searchMemberPost(
                1, PAGE_FETCH_SIZE, today, null, loginMemberId, familyId,
                true, PostType.MISSION
        ).results();
        Map<String, Integer> postUploaderRankMap = new HashMap<>();
        int lastIdx = 1;
        for (Post survivalPost : survivalPosts) {
            postUploaderRankMap.put(survivalPost.getMemberId(), lastIdx++);
        }
        Comparator<FamilyMemberProfileResponse> comparator = Comparator
                .comparing((FamilyMemberProfileResponse member) -> !member.memberId().equals(loginMemberId))
                .thenComparing((FamilyMemberProfileResponse member) -> {
                    Integer rank = postUploaderRankMap.get(member.memberId());
                    return rank != null ? rank : Integer.MAX_VALUE;
                });
        Set<String> pickedSet = new HashSet<>();
        memberPickService.getPickedMembers(familyId, loginMemberId)
                .forEach(pick -> pickedSet.add(pick.getToMemberId()));


        return new MainPageResponse(
                members.stream().sorted(comparator).map((member) -> new MainPageTopBarResponse(
                        member.memberId(),
                        member.imageUrl(),
                        String.valueOf(member.name().charAt(0)),
                        member.name(),
                        postUploaderRankMap.get(member.memberId()),
                        today.isEqual(member.dayOfBirth()),
                        !pickedSet.contains(member.memberId())
                                && !member.memberId().equals(loginMemberId)
                        && !postUploaderRankMap.containsKey(member.memberId())
                )).toList(),
                true,
                survivalPosts.stream().map(post -> {
                    FamilyMemberProfileResponse member = memberMap.get(post.getMemberId());
                    return new MainPageFeedResponse(
                            post.getId(),
                            post.getPostImgUrl(),
                            member != null ? member.name() : "UNKNOWN",
                            post.getCreatedAt().atZone(ZoneId.systemDefault())
                    );
                }).toList(),
                missionPosts.stream().map(post -> {
                    FamilyMemberProfileResponse member = memberMap.get(post.getMemberId());
                    return new MainPageFeedResponse(
                            post.getId(),
                            post.getPostImgUrl(),
                            member != null ? member.name() : "UNKNOWN",
                            post.getCreatedAt().atZone(ZoneId.systemDefault())
                    );
                }).toList()
        );
    }
}
