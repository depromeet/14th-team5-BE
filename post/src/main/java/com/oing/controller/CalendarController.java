package com.oing.controller;

import com.oing.dto.response.ArrayResponse;
import com.oing.dto.response.CalendarResponse;
import com.oing.restapi.CalendarApi;
import org.springframework.stereotype.Controller;

@Controller
public class CalendarController implements CalendarApi {
    @Override
    public ArrayResponse<CalendarResponse> getWeeklyCalendar(String yearMonth, Integer week) {
        return null;
    }

    @Override
    public ArrayResponse<CalendarResponse> getMonthlyCalendar(String yearMonth) {
        return null;
    }
}
