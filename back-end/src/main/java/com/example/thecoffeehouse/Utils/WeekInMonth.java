package com.example.thecoffeehouse.Utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class WeekInMonth {
    /**
     * Tính ngày bắt đầu và kết thúc của tuần trong tháng.
     *
     * @param year năm
     * @param month tháng
     * @param week số tuần (1, 2, 3, ...)
     * @return mảng chứa ngày bắt đầu và kết thúc của tuần
     */
    public static LocalDateTime[] getStartAndEndOfWeekInMonth(int year, int month, int week) {
        // Ngày đầu tiên của tháng
        LocalDate startOfMonth = LocalDate.of(year, month, 1);

        // Ngày đầu tiên của tuần thứ `week` trong tháng
        LocalDate startOfWeek = startOfMonth.withDayOfMonth(1)
                .plusWeeks(week - 1)
                .with(DayOfWeek.MONDAY);

        // Ngày bắt đầu và kết thúc của tuần
        LocalDateTime startDate = startOfWeek.atStartOfDay();
        LocalDateTime endDate = startOfWeek.plusWeeks(1).atStartOfDay().minusNanos(1);

        return new LocalDateTime[] { startDate, endDate };
    }
}
