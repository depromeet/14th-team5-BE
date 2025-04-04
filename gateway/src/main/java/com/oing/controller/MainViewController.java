package com.oing.controller;

import com.oing.domain.*;
import com.oing.dto.response.*;
import com.oing.restapi.MainViewApi;
import com.oing.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/16/24
 * Time: 4:03 PM
 */
@Controller
@RequiredArgsConstructor
public class MainViewController implements MainViewApi {

    private final PostService postService;
    private final MemberService memberService;
    private final MemberPickService memberPickService;
    private final MemberController memberController;
    private final MemberBridge memberBridge;
    private final MissionBridge missionBridge;
    private final PostController postController;
    private final FamilyBridge familyBridge;
    private final MemberNotificationHistoryService memberNotificationHistoryService;

    private static final int PAGE_FETCH_SIZE = 1000;

    @Override
    public DaytimePageResponse getDaytimePage(
            String loginMemberId
    ) {
        String familyId = memberBridge.getFamilyIdByMemberId(loginMemberId);
        LocalDate today = ZonedDateTime.now().toLocalDate();
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

        String todayMissionId = missionBridge.getTodayMissionId();
        String dailyMissionContent = missionBridge.getContentByMissionId(todayMissionId);
        boolean isMissionUnlocked = postController.getMissionAvailableStatus(loginMemberId, loginMemberId, familyId)
                .isMissionUnlocked();
        boolean isMeSurvivalUploadedToday = postController.getSurvivalUploadStatus(loginMemberId, loginMemberId, familyId)
                .isMeSurvivalUploadedToday();
        boolean isMeMissionUploadedToday = postController.getMissionUploadStatus(loginMemberId, loginMemberId, familyId)
                .isMeMissionUploadedToday();
        int leftUploadCountUntilMissionUnlock = postController.getRemainingSurvivalPostCount(loginMemberId, loginMemberId, familyId)
                .leftUploadCountUntilMissionUnlock();
        String familyName = familyBridge.findFamilyNameByFamilyId(familyId);

        return new DaytimePageResponse(
                members.stream().sorted(comparator).map((member) -> new MainPageTopBarResponse(
                        member.memberId(),
                        familyName,
                        member.imageUrl(),
                        String.valueOf(member.name().charAt(0)),
                        member.name(),
                        postUploaderRankMap.get(member.memberId()),
                        member.dayOfBirth().getMonth() == today.getMonth()
                                && member.dayOfBirth().getDayOfMonth() == today.getDayOfMonth(),
                        !pickedSet.contains(member.memberId())
                                && !member.memberId().equals(loginMemberId)
                                && !postUploaderRankMap.containsKey(member.memberId())
                )).toList(),

                memberPickService.getPickMembers(familyId, loginMemberId).stream().map(pickMember -> {
                    FamilyMemberProfileResponse member = memberMap.get(pickMember.getFromMemberId());
                    if (member == null) {
                        return new MainPagePickerResponse(
                                pickMember.getFromMemberId(),
                                null,
                                null
                        );
                    }
                    return new MainPagePickerResponse(
                            member.memberId(),
                            member.imageUrl(),
                            member.name()
                    );
                }).toList(),

                leftUploadCountUntilMissionUnlock,

                isMissionUnlocked,

                isMeSurvivalUploadedToday,

                isMeMissionUploadedToday,

                dailyMissionContent,

                survivalPosts.stream().map(post -> {
                    FamilyMemberProfileResponse member = memberMap.get(post.getMemberId());
                    return new MainPageFeedResponse(
                            post.getId(),
                            post.getPostImgUrl(),
                            member != null ? member.name() : null,
                            post.getCreatedAt().atZone(ZoneId.systemDefault())
                    );
                }).toList(),

                missionPosts.stream().map(post -> {
                    FamilyMemberProfileResponse member = memberMap.get(post.getMemberId());
                    return new MainPageFeedResponse(
                            post.getId(),
                            post.getPostImgUrl(),
                            member != null ? member.name() : null,
                            post.getCreatedAt().atZone(ZoneId.systemDefault())
                    );
                }).toList(),

                memberNotificationHistoryService.findLatestNotificationIdByMemberId(loginMemberId)
        );
    }


    @Override
    public NighttimePageResponse getNighttimePage(String loginMemberId, String loginFamilyId) {
        Page<FamilyMemberProfileResponse> members = memberService.getFamilyMembersProfilesByFamilyId(loginFamilyId, 1, PAGE_FETCH_SIZE);
        LocalDate today = ZonedDateTime.now().toLocalDate();
        String familyName = familyBridge.findFamilyNameByFamilyId(loginFamilyId);
        List<MainPageTopBarResponse> mainPageTopBarResponses = members.stream().map((member) -> new MainPageTopBarResponse(
                member.memberId(),
                familyName,
                member.imageUrl(),
                String.valueOf(member.name().charAt(0)),
                member.name(),
                null,
                member.dayOfBirth().getMonth() == today.getMonth()
                        && member.dayOfBirth().getDayOfMonth() == today.getDayOfMonth(),
                false
        )).toList();

        FamilyMemberMonthlyRankingResponse familyMemberMonthlyRanking = getFamilyMemberMonthlyRanking(loginMemberId, loginFamilyId);

        return new NighttimePageResponse(
                mainPageTopBarResponses,
                familyMemberMonthlyRanking,
                memberNotificationHistoryService.findLatestNotificationIdByMemberId(loginMemberId)
        );
    }


    @Override
    public FamilyMemberMonthlyRankingResponse getFamilyMemberMonthlyRanking(String loginMemberId, String loginFamilyId) {
        List<PostRankerResponse> ranking = postController.getFamilyMembersMonthlySurvivalRanking(loginFamilyId).results().stream().toList();

        FamilyMemberRankerResponse first = null;
        if (ranking.size() >= 1) {
            first = getFamilyMemberRankerResponse(ranking.get(0), loginFamilyId);
        }

        FamilyMemberRankerResponse second = null;
        if (ranking.size() >= 2) {
            second = getFamilyMemberRankerResponse(ranking.get(1), loginFamilyId);
        }

        FamilyMemberRankerResponse third = null;
        if (ranking.size() >= 3) {
            third = getFamilyMemberRankerResponse(ranking.get(2), loginFamilyId);
        }

        LocalDate mostRecentSurvivalPostDate = null;
        LocalDate startOfMonth = ZonedDateTime.now().withDayOfMonth(1).toLocalDate();
        LocalDate tomorrow = ZonedDateTime.now().plusDays(1).toLocalDate();
        PostResponse mostRecentPost = postController.findLatestPost(startOfMonth, tomorrow, PostType.SURVIVAL, loginFamilyId);
        if (mostRecentPost != null) {
            mostRecentSurvivalPostDate = mostRecentPost.createdAt().toLocalDate();
        }

        return new FamilyMemberMonthlyRankingResponse(
                ZonedDateTime.now().getMonthValue(),
                first,
                second,
                third,
                mostRecentSurvivalPostDate
        );
    }

    private FamilyMemberRankerResponse getFamilyMemberRankerResponse(PostRankerResponse postRankerResponse, String loginFamilyId) {
        MemberResponse postRankerMember = memberController.getMember(postRankerResponse.memberId(), loginFamilyId);
        return new FamilyMemberRankerResponse(
                postRankerMember.imageUrl(),
                postRankerMember.name(),
                postRankerResponse.postCount()
        );
    }
}
