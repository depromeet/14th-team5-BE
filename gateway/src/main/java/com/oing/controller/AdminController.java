package com.oing.controller;

import com.oing.domain.DateCountProjection;
import com.oing.dto.response.AdminDailyDashboardResponse;
import com.oing.dto.response.AdminDashboardResponse;
import com.oing.dto.response.DashboardDistributionResponse;
import com.oing.dto.response.DashboardValueResponse;
import com.oing.repository.DashboardRepositoryCustomImpl;
import com.oing.restapi.AdminApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Controller
public class AdminController implements AdminApi {
    private final DashboardRepositoryCustomImpl dashboardRepository;

    @Override
    public AdminDashboardResponse getDashboard() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1L);

        DashboardValueResponse totalMember =
                evaluateValueBetweenYesterday(today, yesterday, dashboardRepository::getTotalMemberCount);
        DashboardValueResponse totalFamily =
                evaluateValueBetweenYesterday(today, yesterday, dashboardRepository::getTotalFamilyCount);
        DashboardValueResponse totalPost =
                evaluateValueBetweenYesterday(today, yesterday, dashboardRepository::getTotalPostCount);
        DashboardValueResponse totalComment =
                evaluateValueBetweenYesterday(today, yesterday, dashboardRepository::getTotalPostCommentCount);
        DashboardValueResponse totalReaction =
                evaluateValueBetweenYesterday(today, yesterday, dashboardRepository::getTotalPostReactionCount);

        List<DashboardDistributionResponse> familyMemberDistribution = calculatePercentage(dashboardRepository
                .getFamilyMemberDistribution())
                .entrySet()
                .stream()
                .map((entry) -> new DashboardDistributionResponse(entry.getKey(), entry.getValue()))
                .toList();


        return new AdminDashboardResponse(
                totalMember, totalFamily, totalPost, totalComment, totalReaction, familyMemberDistribution);
    }

    @Override
    public AdminDailyDashboardResponse getDailyDashboard(LocalDate fromDate, LocalDate toDate) {
        List<DateCountProjection> newMemberCount = dashboardRepository.getNewMemberCount(fromDate, toDate);
        List<DateCountProjection> newPostCount = dashboardRepository.getNewPostCount(fromDate, toDate);

        Map<LocalDate, Integer> dailyMemberRegistration = new HashMap<>();
        Map<LocalDate, Integer> dailyPostCreation = new HashMap<>();

        for (DateCountProjection dateCountProjection : newMemberCount) {
            dailyMemberRegistration.put(dateCountProjection.getDate(), dateCountProjection.getCount().intValue());
        }

        for (DateCountProjection dateCountProjection : newPostCount) {
            dailyPostCreation.put(dateCountProjection.getDate(), dateCountProjection.getCount().intValue());
        }

        return new AdminDailyDashboardResponse(dailyMemberRegistration, dailyPostCreation);
    }

    private DashboardValueResponse evaluateValueBetweenYesterday(
            LocalDate today,
            LocalDate yesterday,
            Function<LocalDate, Long> fetchFunction
    ) {
        long todayValue = fetchFunction.apply(today);
        long yesterdayValue = fetchFunction.apply(yesterday);
        return new DashboardValueResponse(
                (int) todayValue,
                (int) (todayValue - yesterdayValue),
                ((todayValue - yesterdayValue) / (todayValue * 1.0)) * 100.0
        );
    }

    private Map<Integer, Double> calculatePercentage(List<Long> numbers) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        Map<Integer, Double> percentageMap = new HashMap<>();

        // 각 숫자의 출현 횟수를 계산
        for (Long number : numbers) {
            frequencyMap.put(number.intValue(), frequencyMap.getOrDefault(number.intValue(), 0) + 1);
        }

        // 각 숫자의 출현 비율을 계산
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            double percentage = (double) entry.getValue() / numbers.size() * 100;
            percentageMap.put(entry.getKey(), percentage);
        }

        return percentageMap;
    }
}
