package com.oing.controller;

import com.oing.component.TokenAuthenticationHolder;
import com.oing.domain.Member;
import com.oing.domain.MemberPost;
import com.oing.dto.response.SingleRecentPostWidgetResponse;
import com.oing.service.MemberPostService;
import com.oing.service.MemberService;
import com.oing.util.OptimizedImageUrlGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class WidgetControllerTest {

    @InjectMocks
    private WidgetController widgetController;

    @Mock
    private MemberService memberService;
    @Mock
    private MemberPostService memberPostService;
    @Mock
    private TokenAuthenticationHolder tokenAuthenticationHolder;
    @Mock
    private OptimizedImageUrlGenerator optimizedImageUrlGenerator;


    private final Member testMember1 = new Member(
            "testMember1",
            "testFamily",
            LocalDate.of(1999, 10, 18),
            "testMember1",
            "profile.com/1",
            "1"
    );

    private final Member testMember2 = new Member(
            "testMember2",
            "testFamily",
            LocalDate.of(1999, 10, 18),
            "testMember2",
            "profile.com/2",
            "2"
    );

    private final MemberPost testPost1 = new MemberPost(
            "testPost1",
            testMember1.getId(),
            "post.com/1",
            "1",
            "testPost"
    );

    private final List<String> familyIds = List.of(testMember1.getId(), testMember2.getId());


    @Test
    void 최근_게시글_싱글_위젯_정상_조회_테스트() {
        // given
        String date = "2024-10-18";

        when(tokenAuthenticationHolder.getUserId()).thenReturn(testMember1.getId());
        when(memberService.findFamilyMembersIdByMemberId(testMember1.getId())).thenReturn(familyIds);
        when(memberPostService.findLatestPostOfEveryday(familyIds, LocalDate.parse(date), LocalDate.parse(date).plusDays(1))).thenReturn(List.of(testPost1));
        when(memberService.findMemberById(testPost1.getMemberId())).thenReturn(testMember1);
        when(optimizedImageUrlGenerator.getKBImageUrlGenerator(testMember1.getProfileImgUrl())).thenReturn(testMember1.getProfileImgUrl());
        when(optimizedImageUrlGenerator.getKBImageUrlGenerator(testPost1.getPostImgUrl())).thenReturn(testPost1.getPostImgUrl());

        // when
        ResponseEntity<SingleRecentPostWidgetResponse> response = widgetController.getSingleRecentFamilyPostWidget(date);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .extracting(
                        SingleRecentPostWidgetResponse::authorName,
                        SingleRecentPostWidgetResponse::authorProfileImageUrl,
                        SingleRecentPostWidgetResponse::postImageUrl,
                        SingleRecentPostWidgetResponse::postContent
                ).containsExactly(
                        testMember1.getName(),
                        testMember1.getProfileImgUrl(),
                        testPost1.getPostImgUrl(),
                        testPost1.getContent()
                );
    }

    @Test
    void 최근_게시글_싱글_위젯_null_파라미터_조회_테스트() {
        // given
        String date = null;

        when(tokenAuthenticationHolder.getUserId()).thenReturn(testMember1.getId());
        when(memberService.findFamilyMembersIdByMemberId(testMember1.getId())).thenReturn(familyIds);
        when(memberPostService.findLatestPostOfEveryday(familyIds, LocalDate.now(), LocalDate.now().plusDays(1))).thenReturn(List.of(testPost1));
        when(memberService.findMemberById(testPost1.getMemberId())).thenReturn(testMember1);
        when(optimizedImageUrlGenerator.getKBImageUrlGenerator(testMember1.getProfileImgUrl())).thenReturn(testMember1.getProfileImgUrl());
        when(optimizedImageUrlGenerator.getKBImageUrlGenerator(testPost1.getPostImgUrl())).thenReturn(testPost1.getPostImgUrl());

        // when
        ResponseEntity<SingleRecentPostWidgetResponse> response = widgetController.getSingleRecentFamilyPostWidget(date);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .extracting(
                        SingleRecentPostWidgetResponse::authorName,
                        SingleRecentPostWidgetResponse::authorProfileImageUrl,
                        SingleRecentPostWidgetResponse::postImageUrl,
                        SingleRecentPostWidgetResponse::postContent
                ).containsExactly(
                        testMember1.getName(),
                        testMember1.getProfileImgUrl(),
                        testPost1.getPostImgUrl(),
                        testPost1.getContent()
                );
    }

    @Test
    void 최근_게시글_싱글_위젯_빈_게시글_조회_테스트() {
        // given
        String date = "2024-10-18";

        when(tokenAuthenticationHolder.getUserId()).thenReturn(testMember1.getId());
        when(memberService.findFamilyMembersIdByMemberId(testMember1.getId())).thenReturn(familyIds);
        when(memberPostService.findLatestPostOfEveryday(familyIds, LocalDate.parse(date), LocalDate.parse(date).plusDays(1))).thenReturn(List.of());

        // when
        ResponseEntity<SingleRecentPostWidgetResponse> response = widgetController.getSingleRecentFamilyPostWidget(date);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}