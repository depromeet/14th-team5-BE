package com.oing.controller;

import com.oing.component.TokenAuthenticationHolder;
import com.oing.domain.BannerImageType;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostDailyCalendarDTO;
import com.oing.dto.response.ArrayResponse;
import com.oing.dto.response.BannerResponse;
import com.oing.dto.response.CalendarResponse;
import com.oing.dto.response.FamilyMonthlyStatisticsResponse;
import com.oing.restapi.CalendarApi;
import com.oing.service.*;
import com.oing.util.OptimizedImageUrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class CalendarController implements CalendarApi {

    private final MemberService memberService;
    private final MemberPostService memberPostService;
    private final FamilyService familyService;
    private final MemberPostCommentService memberPostCommentService;
    private final MemberPostReactionService memberPostReactionService;
    private final MemberPostRealEmojiService memberPostRealEmojiService;

    private final TokenAuthenticationHolder tokenAuthenticationHolder;
    private final OptimizedImageUrlGenerator optimizedImageUrlGenerator;


    @Override
    @Cacheable(value = "calendarCache", key = "#familyId.concat(':').concat(#yearMonth)", cacheManager = "monthlyCalendarCacheManager")
    public ArrayResponse<CalendarResponse> getMonthlyCalendar(String yearMonth, String familyId) {
        if (yearMonth == null) yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        LocalDate startDate = LocalDate.parse(yearMonth + "-01"); // yyyy-MM-dd 패턴으로 파싱
        LocalDate endDate = startDate.plusMonths(1);
        List<String> familyMembersIds = memberService.findFamilyMembersIdsByFamilyId(familyId);

        List<CalendarResponse> calendarResponses = getCalendarResponses(familyMembersIds, startDate, endDate);
        return new ArrayResponse<>(calendarResponses);
    }

    private List<CalendarResponse> mapPostToCalendar(
            List<MemberPost> representativePosts,
            List<MemberPostDailyCalendarDTO> calendarDTOs,
            int familySize
    ) {
        return IntStream.range(0, representativePosts.size())
                .mapToObj(index -> {
                    MemberPost post = representativePosts.get(index);
                    MemberPostDailyCalendarDTO calendarDTO = calendarDTOs.get(index);

                    LocalDate date = post.getCreatedAt().toLocalDate();
                    String postId = post.getId();
                    String thumbnailUrl = optimizedImageUrlGenerator.getThumbnailUrlGenerator(post.getPostImgUrl());
                    boolean allFamilyMembersUploaded = calendarDTO.dailyPostCount() == familySize;

                    return new CalendarResponse(
                            date,
                            postId,
                            thumbnailUrl,
                            allFamilyMembersUploaded
                    );
                }).toList();
    }

    private List<CalendarResponse> getCalendarResponses(List<String> familyIds, LocalDate startDate, LocalDate endDate) {
        List<MemberPost> representativePosts = memberPostService.findLatestPostOfEveryday(familyIds, startDate, endDate);
        List<MemberPostDailyCalendarDTO> calendarDTOs = memberPostService.findPostDailyCalendarDTOs(familyIds, startDate, endDate);

        return mapPostToCalendar(representativePosts, calendarDTOs, familyIds.size());
    }


    @Override
    public BannerResponse getBanner(String yearMonth, String familyId) {
        /*    파라미터 정리    */
        if (yearMonth == null) yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        LocalDate startDate = LocalDate.parse(yearMonth + "-01"); // yyyy-MM-dd 패턴으로 파싱
        LocalDate endDate = startDate.plusMonths(1);
        List<String> familyMembersIds = memberService.findFamilyMembersIdsByFamilyId(familyId);


        /*    배너를 위한 필드 조회    */
        // 정적 필드 조회
        int familyTopPercentage = familyService.calculateFamilyTopPercentile(familyId);
        int familyPostsCount = (int) memberPostService.countMemberPostsByMemberIdsBetween(familyMembersIds, startDate, endDate);
        int familyInteractionCount = (int) memberPostCommentService.countMemberPostCommentsByMemberIdsBetween(familyMembersIds, startDate, endDate)
                + (int) memberPostReactionService.countMemberPostReactionsByMemberIdsBetween(familyMembersIds, startDate, endDate)
                + (int) memberPostRealEmojiService.countMemberPostRealEmojisByMemberIdsBetween(familyMembersIds, startDate, endDate);

        // 다이나믹 필드 계산
        int allFamilyMembersUploadedDays = 0;
        int allFamilyMembersUploadedStreaks = 0;
        boolean allFamilyMembersUploadedStreaked = true;
        // 한 달 동안 '가족이 전부 올린 날'과 '가족이 전부 올린 날의 연속'을 계산하기 위해, 1일부터 마지막 날까지 순회한다.
        while (startDate.isBefore(endDate)) {
            long postsCount = memberPostService.countMemberPostsByMemberIdsBetween(familyMembersIds, startDate, startDate.plusDays(1));
            long familyMembersCount = memberService.countFamilyMembersByFamilyIdBefore(familyId, startDate.plusDays(1));

            // 가족이 존재한 날만 계산한다.
            if (familyMembersCount != 0) {
                if (postsCount == familyMembersCount) { // 가족 전체가 업로드했다면
                    allFamilyMembersUploadedDays++;

                    if (allFamilyMembersUploadedStreaked) allFamilyMembersUploadedStreaks++; // 가족 전체 업로드가 연속되면, Streak + 1
                } else { // 가족 전체 업로드가 연속되지 못하면, Streak false
                    allFamilyMembersUploadedStreaked = false;
                }
            }

            startDate = startDate.plusDays(1);
        }


        /*    가족의 활성화 레벨을 계산    */
        // [ Level 1 ]  디폴트
        int familyLevel = 1;
        // [ Level 1 기저 조건 ]  업로드 된 글이 없으면, 무조건 Level 1
        if (familyPostsCount == 0) familyLevel = 1;
        // [ Level 4 ]  모두 업로드 20일 이상 or (업로드 사진 60개 이상 and 리액션 120개 이상)
        else if (allFamilyMembersUploadedDays >= 20 || (familyPostsCount >= 60 && familyInteractionCount >= 120)) familyLevel = 4;
        // [ Level 3 ]  이때까지 모두 업로드가 연속되면 OR (업로드 사진 10개이상 and 리액션 10개 이상)
        else if (allFamilyMembersUploadedStreaked || (familyPostsCount >= 10 && familyInteractionCount >= 10)) familyLevel = 3;
        // [ Level 2 ]  모두 업로드 한 날이 1일 이상 OR 업로드된 사진 2개 이상
        else if (allFamilyMembersUploadedDays >= 1 || familyPostsCount >= 2) familyLevel = 2;


        /*    배너 이미지 결정    */
        BannerImageType bannerImageType;
        if (familyLevel == 1) bannerImageType = BannerImageType.SKULL_FLAG;
        else if (familyLevel == 2) bannerImageType = BannerImageType.ALONE_WALKING;
        else if (familyLevel == 3) bannerImageType = BannerImageType.WE_ARE_FRIENDS;
        else if (familyLevel == 4) bannerImageType = BannerImageType.JEWELRY_TREASURE;
        else bannerImageType = BannerImageType.SKULL_FLAG; // 예외 처리


        return new BannerResponse(
                familyTopPercentage,
                allFamilyMembersUploadedDays,
                familyLevel,
                bannerImageType
        );
    }

    @Override
    public FamilyMonthlyStatisticsResponse getSummary(String yearMonth) {
        String memberId = tokenAuthenticationHolder.getUserId();
        String[] yearMonthArray = yearMonth.split("-");
        int year = Integer.parseInt(yearMonthArray[0]);
        int month = Integer.parseInt(yearMonthArray[1]);

        String familyId = memberService.findFamilyIdByMemberId(memberId);
        long monthlyPostCount = memberPostService.countMonthlyPostByFamilyId(year, month, familyId);
        return new FamilyMonthlyStatisticsResponse(
                (int) monthlyPostCount
        );
    }
}
