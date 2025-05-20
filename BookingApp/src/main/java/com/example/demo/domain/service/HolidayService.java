package com.example.demo.domain.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class HolidayService {
    
    public boolean isSchoolHoliday(LocalDate date) {
        // 学校休館日判定ロジック
        // 例えば、祝日や特定の日付をチェックする
        return false;  // 仮の実装
    }
}
