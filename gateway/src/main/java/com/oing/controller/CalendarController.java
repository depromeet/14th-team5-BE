package com.oing.controller;

import com.oing.component.TokenAuthenticationHolder;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostDailyCalendarDTO;
import com.oing.dto.response.ArrayResponse;
import com.oing.dto.response.CalendarResponse;
import com.oing.restapi.CalendarApi;
import com.oing.service.MemberPostService;
import com.oing.service.MemberService;
import com.oing.util.OptimizedImageUrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class CalendarController implements CalendarApi {

    private final MemberService memberService;
    private final MemberPostService memberPostService;

    private final TokenAuthenticationHolder tokenAuthenticationHolder;
    private final OptimizedImageUrlGenerator optimizedImageUrlGenerator;


    private List<String> getFamilyIds() {
        String myId = tokenAuthenticationHolder.getUserId();
        return memberService.findFamilyMembersIdByMemberId(myId);
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
