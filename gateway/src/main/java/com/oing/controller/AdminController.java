package com.oing.controller;

import com.oing.dto.response.AdminDailyDashboardResponse;
import com.oing.dto.response.AdminDashboardResponse;
import com.oing.restapi.AdminApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@RequiredArgsConstructor
@Controller
public class AdminController implements AdminApi {
    @Override
    public AdminDashboardResponse getDashboard() {
        return null;
    }

    @Override
    public AdminDailyDashboardResponse getDailyDashboard(LocalDate fromDate, LocalDate toDate) {
        return null;
    }
}
