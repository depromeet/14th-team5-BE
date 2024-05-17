package com.oing.controller;

import com.oing.domain.BannerImageType;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.oing.dto.response.*;
import com.oing.restapi.CalendarApi;
import com.oing.service.*;
import com.oing.util.OptimizedImageUrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.oing.domain.PostType.MISSION;
import static com.oing.domain.PostType.SURVIVAL;

@Controller
@RequiredArgsConstructor
public class CalendarController implements CalendarApi {

    private final PostController postController;
    private final MissionBridge missionBridge;
    private final MemberBridge memberBridge;

    private final MemberService memberService;
    private final PostService postService;
    private final FamilyService familyService;

    private final OptimizedImageUrlGenerator optimizedImageUrlGenerator;


    @Override
    public ArrayResponse<DailyCalendarResponse> getDailyCalendar(String yearMonthDay, String loginMemberId, String loginFamilyId) {
        List<DailyCalendarResponse> dailyCalendarResponses = new ArrayList<>();
        LocalDate date = LocalDate.parse(yearMonthDay, DateTimeFormatter.ISO_DATE);

        Collection<PostResponse> survivalPosts = postController.fetchDailyFeeds(1, 10, date, null, "ASC", SURVIVAL, loginMemberId).results();
        Collection<PostResponse> missionPosts = postController.fetchDailyFeeds(1, 10, date, null, "ASC", MISSION, loginMemberId).results();
        String missionContent = missionBridge.getContentByDate(date);
        boolean allFamilyMembersUploaded = getAllFamilyMembersUploaded(survivalPosts, loginFamilyId);

        dailyCalendarResponses.addAll(convertToDailyCalendarResponse(survivalPosts, missionContent, allFamilyMembersUploaded));
        dailyCalendarResponses.addAll(convertToDailyCalendarResponse(missionPosts, missionContent, allFamilyMembersUploaded));
        return ArrayResponse.of(dailyCalendarResponses);
    }

    private boolean getAllFamilyMembersUploaded(Collection<PostResponse> survivalPosts, String familyId) {
        HashSet<String> uploadedFamilyMembers = survivalPosts.stream().map(PostResponse::authorId).collect(Collectors.toCollection(HashSet::new));
        List<String> familyMembersIds = memberService.getFamilyMembersIdsByFamilyIdAndJoinAtBefore(familyId, LocalDate.now());

        return uploadedFamilyMembers.containsAll(familyMembersIds);
    }

    private List<DailyCalendarResponse> convertToDailyCalendarResponse(Collection<PostResponse> posts, String missionContent, boolean allFamilyMembersUploaded) {
        return posts.stream().map(post -> switch (PostType.fromString(post.type())) {
            case MISSION -> DailyCalendarResponse.of(post, missionContent, allFamilyMembersUploaded);
            case SURVIVAL -> DailyCalendarResponse.of(post, null, allFamilyMembersUploaded);
        }).toList();
    }


    @Override
    public ArrayResponse<MonthlyCalendarResponse> getMonthlyCalendar(String yearMonth, String familyId) {
        if (yearMonth == null) yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        LocalDate startDate = LocalDate.parse(yearMonth + "-01"); // yyyy-MM-dd 패턴으로 파싱
        LocalDate endDate = startDate.plusMonths(1);

        List<Post> daysLatestPosts = postService.findOldestPostOfEveryday(startDate, endDate, familyId);
        List<MonthlyCalendarResponse> monthlyCalendarResponses = convertToMonthlyCalendarResponse(daysLatestPosts, familyId);
        return new ArrayResponse<>(monthlyCalendarResponses);
    }

    private List<MonthlyCalendarResponse> convertToMonthlyCalendarResponse(List<Post> daysLatestPosts, String familyId) {
        List<MonthlyCalendarResponse> monthlyCalendarResponses = new ArrayList<>();

        for (Post dayLatestPost : daysLatestPosts) {
            LocalDate postDate = dayLatestPost.getCreatedAt().toLocalDate();

            // 탈퇴한 회원을 제외하고 allFamilyMembersUploaded 기본값이 true이므로, 탈퇴한 회원이 allFamilyMembersUploaded 계산에 영향을 미치지 않음
            // edge case: 글을 업로드하지 않은 회원이 탈퇴하면, 과거 날짜들의 allFamilyMembersUploaded이 true로 변함 -> 핸들링할 수 없는 케이스
            List<String> familyMembersIds = memberService.getFamilyMembersIdsByFamilyIdAndJoinAtBefore(familyId, postDate.plusDays(1));
            boolean allFamilyMembersUploaded = true;
            for (String memberId : familyMembersIds) {
                if (!postService.existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(memberId, familyId, SURVIVAL, postDate)) {
                    allFamilyMembersUploaded = false;
                    break;
                }
            }

            monthlyCalendarResponses.add(new MonthlyCalendarResponse(
                    dayLatestPost.getCreatedAt().toLocalDate(),
                    dayLatestPost.getId(),
                    optimizedImageUrlGenerator.getThumbnailUrlGenerator(dayLatestPost.getPostImgUrl()),
                    allFamilyMembersUploaded
            ));
        }
        return monthlyCalendarResponses;
    }


    @Override
    public ArrayResponse<MonthlyCalendarResponse> getMonthlyCalendarLegacy(String yearMonth, String familyId) {
        return getMonthlyCalendar(yearMonth, familyId);
    }


    @Override
    public BannerResponse getBanner(String yearMonth, String familyId) {
        /*    파라미터 정리    */
        if (yearMonth == null) yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        LocalDate startDate = LocalDate.parse(yearMonth + "-01"); // yyyy-MM-dd 패턴으로 파싱
        LocalDate endDate = startDate.plusMonths(1);


        /*    배너를 위한 필드 조회    */
        /*  정적 필드 조회 */
        int familyTopPercentage = familyService.getFamilyTopPercentage(familyId, startDate);
        List<Post> familyPosts = postService.findAllByFamilyIdAndCreatedAtBetween(familyId, startDate, endDate);
        int familyPostsCount = familyPosts.size();
        int familyInteractionCount = familyPosts.stream().mapToInt((memberPost -> memberPost.getCommentCnt() + memberPost.getReactionCnt() + memberPost.getRealEmojiCnt())).sum();

        /* 다이나믹 필드 계산 */
        int allFamilyMembersUploadedDays = 0;
        int allFamilyMembersUploadedStreaks = 0;
        boolean allFamilyMembersUploadedStreaked = true;

        // 한 달 동안 '가족이 전부 업로드한 날'과 '전부 업로드한 날의 연속'을 계산하기 위해, 1일부터 마지막 날까지 순회한다.
        while (startDate.isBefore(endDate)) {
            boolean allFamilyMembersUploaded = true;

            if (postService.existsByFamilyIdAndCreatedAt(familyId, startDate)) {
                List<String> familyMembersIds = memberService.getFamilyMembersIdsByFamilyIdAndJoinAtBefore(familyId, startDate.plusDays(1));
                for (String memberId : familyMembersIds) {
                    if (!postService.existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(memberId, familyId, SURVIVAL, startDate)) {
                        allFamilyMembersUploaded = false;
                        break;
                    }
                }

            } else {
                // 게시글이 없다면, 계산에서 제외
                // 해당 날짜에 가족과 게시물이 없는 경우, allFamilyMembersUploaded = true 가 되는 것을 방지
                // edge case: 게시물을 하나라도 없로드 하고 글을 업로드하지 않은 회원이 탈퇴하면, allFamilyMembersUploaded이 true로 변함 -> 핸들링할 수 없는 케이스
                allFamilyMembersUploaded = false;
            }


            // (가족 전부 업로드한 날의 수, 연속해서 업로드한 여부, 연속해서 업로드한 날의 수) 계산
            if (allFamilyMembersUploaded) {
                allFamilyMembersUploadedDays++;

                if (allFamilyMembersUploadedStreaked)
                    allFamilyMembersUploadedStreaks++;
            } else {
                allFamilyMembersUploadedStreaked = false;
            }

            startDate = startDate.plusDays(1);
        }

        int familyLevel = getFamilyLevel(familyPostsCount, allFamilyMembersUploadedDays, familyInteractionCount, allFamilyMembersUploadedStreaked);
        BannerImageType bannerImageType = getBannerImageType(familyLevel);

        return new BannerResponse(familyTopPercentage, allFamilyMembersUploadedDays, familyLevel, bannerImageType);
    }

    private int getFamilyLevel(int familyPostsCount, int allFamilyMembersUploadedDays, int familyInteractionCount, boolean allFamilyMembersUploadedStreaked) {
        int familyLevel = 1;

        // [ Level 1 기저 조건 ]  업로드 된 글이 없으면, 무조건 Level 1
        if (familyPostsCount == 0) familyLevel = 1;

            // [ Level 4 ]  모두 업로드 20일 이상 or (업로드 사진 60개 이상 and 리액션 120개 이상)
        else if (allFamilyMembersUploadedDays >= 20 || (familyPostsCount >= 60 && familyInteractionCount >= 120))
            familyLevel = 4;

            // [ Level 3 ]  이때까지 모두 업로드가 연속되면 OR (업로드 사진 10개이상 and 리액션 10개 이상)
        else if (allFamilyMembersUploadedStreaked || (familyPostsCount >= 10 && familyInteractionCount >= 10))
            familyLevel = 3;

            // [ Level 2 ]  모두 업로드 한 날이 1일 이상 OR 업로드된 사진 2개 이상
        else if (allFamilyMembersUploadedDays >= 1 || familyPostsCount >= 2) familyLevel = 2;

        return familyLevel;
    }

    private BannerImageType getBannerImageType(int familyLevel) {
        BannerImageType bannerImageType;
        if (familyLevel == 1) bannerImageType = BannerImageType.SKULL_FLAG;
        else if (familyLevel == 2) bannerImageType = BannerImageType.ALONE_WALKING;
        else if (familyLevel == 3) bannerImageType = BannerImageType.WE_ARE_FRIENDS;
        else if (familyLevel == 4) bannerImageType = BannerImageType.JEWELRY_TREASURE;
        else bannerImageType = BannerImageType.SKULL_FLAG; // 예외 처리

        return bannerImageType;
    }


    @Override
    public FamilyMonthlyStatisticsResponse getSummary(String yearMonth, String loginMemberId) {
        String[] yearMonthArray = yearMonth.split("-");
        int year = Integer.parseInt(yearMonthArray[0]);
        int month = Integer.parseInt(yearMonthArray[1]);

        String familyId = memberService.getFamilyIdByMemberId(loginMemberId);
        long monthlyPostCount = postService.countMonthlyPostByFamilyId(year, month, familyId);
        return new FamilyMonthlyStatisticsResponse((int) monthlyPostCount);
    }
}
