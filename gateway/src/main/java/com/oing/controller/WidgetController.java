package com.oing.controller;


import com.oing.component.TokenAuthenticationHolder;
import com.oing.domain.Member;
import com.oing.domain.MemberPost;
import com.oing.dto.response.SingleRecentPostWidgetResponse;
import com.oing.restapi.WidgetApi;
import com.oing.service.MemberPostService;
import com.oing.service.MemberService;
import com.oing.util.OptimizedImageUrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class WidgetController implements WidgetApi {

    private final MemberService memberService;
    private final MemberPostService memberPostService;

    private final TokenAuthenticationHolder tokenAuthenticationHolder;
    private final OptimizedImageUrlGenerator optimizedImageUrlGenerator;

    @Override
    public ResponseEntity<SingleRecentPostWidgetResponse> getSingleRecentFamilyPostWidget(String date) {
        String myId = tokenAuthenticationHolder.getUserId();
        List<String> familyIds = memberService.findFamilyMembersIdByMemberId(myId);

        Optional<String> dateString = Optional.ofNullable(date);
        LocalDate startDate = dateString.map(LocalDate::parse).orElse(LocalDate.now());
        LocalDate endDate = startDate.plusDays(1);


        List<MemberPost> latestPosts = memberPostService.findLatestPostOfEveryday(familyIds, startDate, endDate);
        if (latestPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        MemberPost latestPost = latestPosts.get(0);
        Member author = memberService.findMemberById(latestPost.getMemberId());
        return ResponseEntity.ok(new SingleRecentPostWidgetResponse(
                author.getName(),
                optimizedImageUrlGenerator.getKBImageUrlGenerator(author.getProfileImgUrl()),
                optimizedImageUrlGenerator.getKBImageUrlGenerator(latestPost.getPostImgUrl()),
                latestPost.getContent()
        ));
    }
}
