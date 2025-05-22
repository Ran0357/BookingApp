package com.example.demo.presentation.api;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.service.HolidayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/facility")
public class HolidayController {

    private final HolidayService holidayService;

    @GetMapping("/{facilityId}/closed-dates")
    public List<String> getClosedDates(
            @PathVariable int facilityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        return holidayService.getClosedDates(facilityId, start, end)
                .stream()
                .map(LocalDate::toString) // "yyyy-MM-dd"
                .collect(Collectors.toList());
    }
}
