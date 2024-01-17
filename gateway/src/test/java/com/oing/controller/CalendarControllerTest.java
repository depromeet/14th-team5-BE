package com.oing.controller;

import com.oing.component.TokenAuthenticationHolder;
import com.oing.domain.Member;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostDailyCalendarDTO;
import com.oing.dto.response.ArrayResponse;
import com.oing.dto.response.CalendarResponse;
import com.oing.service.MemberPostService;
import com.oing.service.MemberService;
import com.oing.util.OptimizedImageUrlGenerator;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            "1", ""
    );

    private final Member testMember2 = new Member(
            "testMember2",
            "testFamily",
            LocalDate.of(1999, 10, 18),
            "testMember2",
            "profile.com/2",
            "2", ""
    );

    private final List<String> familyIds = List.of(testMember1.getId(), testMember2.getId());


    @Test
    void 주간_캘린더_조회_테스트() {
        // Given
        String yearMonth = "2023-11";
        Integer week = 1;

        LocalDate startDate = LocalDate.of(2023, 11, 1);
        LocalDate endDate = startDate.plusWeeks(1);
        MemberPost testPost1 = new MemberPost(
                "1",
                testMember1.getId(),
                "post.com/1",
                "1",
                "test1"
        );
        ReflectionTestUtils.setField(testPost1, "createdAt", LocalDateTime.of(2023, 11, 1, 13, 0));
        MemberPost testPost2 = new MemberPost(
                "2",
                testMember2.getId(),
                "post.com/2",
                "2",
                "test2"
        );
        ReflectionTestUtils.setField(testPost2, "createdAt", LocalDateTime.of(2023, 11, 2, 13, 0));
        List<MemberPost> representativePosts = List.of(testPost1, testPost2);
        List<MemberPostDailyCalendarDTO> calendarDTOs = List.of(
                new MemberPostDailyCalendarDTO(2L),
                new MemberPostDailyCalendarDTO(1L)
        );
        when(tokenAuthenticationHolder.getUserId()).thenReturn(testMember1.getId());
        when(memberService.findFamilyMembersIdByMemberId(testMember1.getId())).thenReturn(familyIds);
        when(memberPostService.findLatestPostOfEveryday(familyIds, startDate, endDate)).thenReturn(representativePosts);
        when(memberPostService.findPostDailyCalendarDTOs(familyIds, startDate, endDate)).thenReturn(calendarDTOs);

        // When
        ArrayResponse<CalendarResponse> weeklyCalendar = calendarController.getWeeklyCalendar(yearMonth, week);

        // Then
        assertThat(weeklyCalendar.results())
                .extracting(CalendarResponse::representativePostId, CalendarResponse::allFamilyMembersUploaded)
                .containsExactly(
                        Tuple.tuple("1", true),
                        Tuple.tuple("2", false)
                );
    }

    @Test
    void 주간_캘린더_파라미터_없이_조회_테스트() {
        // Given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusWeeks(1);
        MemberPost testPost1 = new MemberPost(
                "1",
                testMember1.getId(),
                "post.com/1",
                "1",
                "test1"
        );
        ReflectionTestUtils.setField(testPost1, "createdAt", LocalDateTime.now());
        List<MemberPost> representativePosts = List.of(testPost1);
        List<MemberPostDailyCalendarDTO> calendarDTOs = List.of(
                new MemberPostDailyCalendarDTO(1L)
        );
        when(tokenAuthenticationHolder.getUserId()).thenReturn(testMember1.getId());
        when(memberService.findFamilyMembersIdByMemberId(testMember1.getId())).thenReturn(familyIds);
        when(memberPostService.findLatestPostOfEveryday(familyIds, startDate, endDate)).thenReturn(representativePosts);
        when(memberPostService.findPostDailyCalendarDTOs(familyIds, startDate, endDate)).thenReturn(calendarDTOs);

        // When
        ArrayResponse<CalendarResponse> weeklyCalendar = calendarController.getWeeklyCalendar(null, null);

        // Then
        assertThat(weeklyCalendar.results())
                .extracting(CalendarResponse::representativePostId, CalendarResponse::allFamilyMembersUploaded)
                .containsExactly(
                        Tuple.tuple("1", false)
                );
    }

    @Test
    void 월별_캘린더_조회_테스트() {
        // Given
        String yearMonth = "2023-11";

        LocalDate startDate = LocalDate.of(2023, 11, 1);
        LocalDate endDate = startDate.plusMonths(1);
        MemberPost testPost1 = new MemberPost(
                "1",
                testMember1.getId(),
                "post.com/1",
                "1",
                "test1"
        );
        ReflectionTestUtils.setField(testPost1, "createdAt", LocalDateTime.of(2023, 11, 1, 13, 0));
        MemberPost testPost2 = new MemberPost(
                "2",
                testMember2.getId(),
                "post.com/2",
                "2",
                "test2"
        );
        ReflectionTestUtils.setField(testPost2, "createdAt", LocalDateTime.of(2023, 11, 2, 13, 0));
        MemberPost testPost3 = new MemberPost(
                "3",
                testMember1.getId(),
                "post.com/3",
                "3",
                "test3"
        );
        ReflectionTestUtils.setField(testPost3, "createdAt", LocalDateTime.of(2023, 11, 8, 13, 0));
        MemberPost testPost4 = new MemberPost(
                "4",
                testMember2.getId(),
                "post.com/4",
                "4",
                "test4"
        );
        ReflectionTestUtils.setField(testPost4, "createdAt", LocalDateTime.of(2023, 11, 9, 13, 0));
        List<MemberPost> representativePosts = List.of(testPost1, testPost2, testPost3, testPost4);
        List<MemberPostDailyCalendarDTO> calendarDTOs = List.of(
                new MemberPostDailyCalendarDTO(2L),
                new MemberPostDailyCalendarDTO(1L),
                new MemberPostDailyCalendarDTO(2L),
                new MemberPostDailyCalendarDTO(1L)
        );
        when(tokenAuthenticationHolder.getUserId()).thenReturn(testMember1.getId());
        when(memberService.findFamilyMembersIdByMemberId(testMember1.getId())).thenReturn(familyIds);
        when(memberPostService.findLatestPostOfEveryday(familyIds, startDate, endDate)).thenReturn(representativePosts);
        when(memberPostService.findPostDailyCalendarDTOs(familyIds, startDate, endDate)).thenReturn(calendarDTOs);

        // When
        ArrayResponse<CalendarResponse> weeklyCalendar = calendarController.getMonthlyCalendar(yearMonth);

        // Then
        assertThat(weeklyCalendar.results())
                .extracting(CalendarResponse::representativePostId, CalendarResponse::allFamilyMembersUploaded)
                .containsExactly(
                        Tuple.tuple("1", true),
                        Tuple.tuple("2", false),
                        Tuple.tuple("3", true),
                        Tuple.tuple("4", false)
                );
    }

    @Test
    void 월별_캘린더_파라미터_없이_조회_테스트() {
        // Given
        LocalDate startDate = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-01");
        LocalDate endDate = startDate.plusMonths(1);
        MemberPost testPost1 = new MemberPost(
                "1",
                testMember1.getId(),
                "post.com/1",
                "1",
                "test1"
        );
        ReflectionTestUtils.setField(testPost1, "createdAt", LocalDateTime.now());
        List<MemberPost> representativePosts = List.of(testPost1);
        List<MemberPostDailyCalendarDTO> calendarDTOs = List.of(
                new MemberPostDailyCalendarDTO(1L)
        );
        when(tokenAuthenticationHolder.getUserId()).thenReturn(testMember1.getId());
        when(memberService.findFamilyMembersIdByMemberId(testMember1.getId())).thenReturn(familyIds);
        when(memberPostService.findLatestPostOfEveryday(familyIds, startDate, endDate)).thenReturn(representativePosts);
        when(memberPostService.findPostDailyCalendarDTOs(familyIds, startDate, endDate)).thenReturn(calendarDTOs);

        // When
        ArrayResponse<CalendarResponse> weeklyCalendar = calendarController.getMonthlyCalendar(null);

        // Then
        assertThat(weeklyCalendar.results())
                .extracting(CalendarResponse::representativePostId, CalendarResponse::allFamilyMembersUploaded)
                .containsExactly(
                        Tuple.tuple("1", false)
                );
    }
}
