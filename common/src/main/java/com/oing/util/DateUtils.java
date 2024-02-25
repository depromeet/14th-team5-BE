package com.oing.util;

import java.time.LocalDate;

public class DateUtils {

    public static boolean isCurrentMonth(LocalDate date) {
        return isSameMonth(date, LocalDate.now());
    }

    public static boolean isSameMonth(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth();
    }
}
