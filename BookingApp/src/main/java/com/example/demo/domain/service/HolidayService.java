package com.example.demo.domain.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class HolidayService {

    public boolean isFacilityClosed(int facilityId, LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();

        switch (facilityId) {
            case 1: // カラオケ：月火木金のみ
                return !(day == DayOfWeek.MONDAY || day == DayOfWeek.TUESDAY
                      || day == DayOfWeek.THURSDAY || day == DayOfWeek.FRIDAY);
            case 3: // ブース：月〜金のみ
                return (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
            default:
                return true; // 未定義施設は全休
        }
    }

    public List<LocalDate> getClosedDates(int facilityId, LocalDate start, LocalDate end) {
        List<LocalDate> closedDates = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (isFacilityClosed(facilityId, date)) {
                closedDates.add(date);
            }
        }
        return closedDates;
    }
}
