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


    @Override
    public ArrayResponse<CalendarResponse> getWeeklyCalendar(String yearMonth, Integer week) {
        return null;
    }

    @Override
    public ArrayResponse<CalendarResponse> getMonthlyCalendar(String yearMonth) {
        // 가족구성원들의 id를 로드한다.
        String memberId = tokenAuthenticationHolder.getUserId();
        List<String> familyIds = memberService.findFamilyMemberIdByMemberId(memberId);


        // 한 달 동안, 매일을 대표하는 게시물과 그 날 업로드된 게시글 개수를 로드한다.
        LocalDate startDate = LocalDate.parse(yearMonth + "-01");
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<MemberPost> representativePosts = memberPostService.findLatestFamilyPostOfEveryday(familyIds, startDate, endDate);
        List<MemberPostCountDTO> postCounts = memberPostService.countFamilyPostsOfEveryday(familyIds, startDate, endDate);


        // 대표 게시글과 게시글 개수를 CalendarResponse 에 매핑한다.
        List<CalendarResponse> calendar = new ArrayList<>();
        for (int index = 0; index < representativePosts.size(); index++) {
            MemberPost post = representativePosts.get(index);
            MemberPostCountDTO postCount = postCounts.get(index);

            LocalDate date = post.getCreatedAt().toLocalDate();
            String postId = post.getId();
            String thumbnailUrl = optimizedImageUrlProvider.getThumbnailUrlGenerator(post.getImageUrl());
            boolean allFamilyMembersUploaded = postCount.count() == familyIds.size();


            calendar.add(
                    new CalendarResponse(
                            date,
                            postId,
                            thumbnailUrl,
                            allFamilyMembersUploaded
                    )
            );
        }

        return new ArrayResponse<>(calendar);
    }
}
