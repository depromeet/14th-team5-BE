package com.oing.controller;


import com.oing.domain.Member;
import com.oing.domain.Post;
import com.oing.dto.response.SingleRecentPostWidgetResponse;
import com.oing.restapi.WidgetApi;
import com.oing.service.MemberService;
import com.oing.service.PostService;
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
    private final PostService postService;

    private final OptimizedImageUrlGenerator optimizedImageUrlGenerator;

    @Override
    public ResponseEntity<SingleRecentPostWidgetResponse> getSingleRecentFamilyPostWidget(String date, String loginFamilyId) {
        Optional<String> dateString = Optional.ofNullable(date);
        LocalDate startDate = dateString.map(LocalDate::parse).orElse(LocalDate.now());
        LocalDate endDate = startDate.plusDays(1);


        List<Post> latestPosts = postService.findOldestPostOfEveryday(startDate, endDate, loginFamilyId);
        if (latestPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Post latestPost = latestPosts.get(0);
        Member author = memberService.getMemberByMemberId(latestPost.getMemberId());
        return ResponseEntity.ok(new SingleRecentPostWidgetResponse(
                author.getName(),
                optimizedImageUrlGenerator.getKBImageUrlGenerator(author.getProfileImgUrl()),
                latestPost.getId(),
                optimizedImageUrlGenerator.getKBImageUrlGenerator(latestPost.getPostImgUrl()),
                latestPost.getContent()
        ));
    }
}
