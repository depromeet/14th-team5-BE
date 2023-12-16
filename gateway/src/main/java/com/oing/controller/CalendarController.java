package com.oing.controller;

import com.oing.component.TokenAuthenticationHolder;
import com.oing.config.support.OptimizedImageUrlProvider;
import com.oing.domain.MemberPostCountDTO;
import com.oing.domain.model.MemberPost;
import com.oing.dto.response.ArrayResponse;
import com.oing.dto.response.CalendarResponse;
import com.oing.restapi.CalendarApi;
import com.oing.service.MemberPostService;
import com.oing.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CalendarController implements CalendarApi {

    private final MemberService memberService;
    private final MemberPostService memberPostService;

    private final TokenAuthenticationHolder tokenAuthenticationHolder;
    private final OptimizedImageUrlProvider optimizedImageUrlProvider;


    private List<String> getFamilyIds() {
        String myId = tokenAuthenticationHolder.getUserId();
        return memberService.findFamilyMemberIdByMemberId(myId);
    }

    private List<CalendarResponse> mapPostToCalendar(List<MemberPost> representativePosts, List<MemberPostCountDTO> postCounts, int familySize) {
        List<CalendarResponse> calendar = new ArrayList<>();
        for (int index = 0; index < representativePosts.size(); index++) {
            MemberPost post = representativePosts.get(index);
            MemberPostCountDTO postCount = postCounts.get(index);

            LocalDate date = post.getCreatedAt().toLocalDate();
            String postId = post.getId();
            String thumbnailUrl = optimizedImageUrlProvider.getThumbnailUrlGenerator(post.getImageUrl());
            boolean allFamilyMembersUploaded = postCount.count() == familySize;


            calendar.add(
                    new CalendarResponse(
                            date,
                            postId,
                            thumbnailUrl,
                            allFamilyMembersUploaded
                    )
            );
        }

        return calendar;
    }

    private List<CalendarResponse> getCalendarResponses(List<String> familyIds, LocalDate startDate, LocalDate endDate) {
        List<MemberPost> representativePosts = memberPostService.findLatestPostOfEveryday(familyIds, startDate, endDate);
        List<MemberPostCountDTO> postCounts = memberPostService.countPostsOfEveryday(familyIds, startDate, endDate);

        return mapPostToCalendar(representativePosts, postCounts, familyIds.size());
    }

    @Override
    public ArrayResponse<CalendarResponse> getWeeklyCalendar(String yearMonth, Long week) {
        List<String> familyIds = getFamilyIds();
        LocalDate startDate = LocalDate.parse(yearMonth + "-01").plusWeeks(week - 1);
        LocalDate endDate = startDate.plusWeeks(1);

        List<CalendarResponse> calendarResponses = getCalendarResponses(familyIds, startDate, endDate);
        return new ArrayResponse<>(calendarResponses);
    }

    @Override
    public ArrayResponse<CalendarResponse> getMonthlyCalendar(String yearMonth) {
        List<String> familyIds = getFamilyIds();
        LocalDate startDate = LocalDate.parse(yearMonth + "-01");
        LocalDate endDate = startDate.plusMonths(1);

        List<CalendarResponse> calendarResponses = getCalendarResponses(familyIds, startDate, endDate);
        return new ArrayResponse<>(calendarResponses);
    }
}
