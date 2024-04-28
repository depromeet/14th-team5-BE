package com.oing.controller;

import com.oing.domain.Member;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.oing.dto.response.ArrayResponse;
import com.oing.dto.response.MonthlyCalendarResponse;
import com.oing.service.MemberService;
import com.oing.service.PostService;
import com.oing.util.OptimizedImageUrlGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CalendarControllerTest {

    @InjectMocks
    private CalendarController calendarController;

    @Mock
    private MemberService memberService;
    @Mock
    private PostService postService;
    @Mock
    private OptimizedImageUrlGenerator optimizedImageUrlGenerator;


    private final Member testMember1 = new Member(
            "testMember1",
            "testFamily",
            LocalDate.of(1999, 10, 18),
            "testMember1",
            "profile.com/1",
            "1",
            LocalDateTime.of(2023, 11, 1, 13, 0)
    );

    private final Member testMember2 = new Member(
            "testMember2",
            "testFamily",
            LocalDate.of(1999, 10, 18),
            "testMember2",
            "profile.com/2",
            "2",
            LocalDateTime.of(2023, 11, 2, 13, 0)
    );


    @Test
    void 월간_캘린더_조회_테스트() {
        // Given
        String yearMonth = "2023-11";
        String familyId = testMember1.getFamilyId();

        LocalDate startDate = LocalDate.of(2023, 11, 1);
        LocalDate endDate = startDate.plusMonths(1);
        Post testPost1 = new Post(
                "1",
                testMember1.getId(),
                familyId,
                PostType.SURVIVAL,
                "post.com/1",
                "1",
                "test1"
        );
        ReflectionTestUtils.setField(testPost1, "createdAt", LocalDateTime.of(2023, 11, 1, 13, 0));
        Post testPost2 = new Post(
                "2",
                testMember2.getId(),
                familyId,
                PostType.SURVIVAL,
                "post.com/2",
                "2",
                "test2"
        );
        ReflectionTestUtils.setField(testPost2, "createdAt", LocalDateTime.of(2023, 11, 2, 13, 0));
        Post testPost3 = new Post(
                "3",
                testMember1.getId(),
                familyId,
                PostType.SURVIVAL,
                "post.com/3",
                "3",
                "test3"
        );
        ReflectionTestUtils.setField(testPost3, "createdAt", LocalDateTime.of(2023, 11, 8, 13, 0));
        Post testPost4 = new Post(
                "4",
                testMember2.getId(),
                familyId,
                PostType.SURVIVAL,
                "post.com/4",
                "4",
                "test4"
        );
        ReflectionTestUtils.setField(testPost4, "createdAt", LocalDateTime.of(2023, 11, 9, 13, 0));
        List<Post> representativePosts = List.of(testPost1, testPost2, testPost3, testPost4);
        when(postService.findLatestPostOfEveryday(startDate, endDate, familyId)).thenReturn(representativePosts);

        // When
        ArrayResponse<MonthlyCalendarResponse> weeklyCalendar = calendarController.getMonthlyCalendar(yearMonth, familyId);

        // Then
        assertThat(weeklyCalendar.results())
                .extracting(MonthlyCalendarResponse::representativePostId)
                .containsExactly("1", "2", "3", "4");
    }
}
