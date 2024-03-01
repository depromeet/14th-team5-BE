package com.oing.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DateCountProjection {
    private LocalDate date;
    private Long count;

    public DateCountProjection(Object date, Long count) {
        if (date instanceof Date sqlDate) {
            this.date = sqlDate.toLocalDate();
        } else if (date instanceof LocalDate localDate) {
            this.date = localDate;
        }
        this.count = count;
    }
}
