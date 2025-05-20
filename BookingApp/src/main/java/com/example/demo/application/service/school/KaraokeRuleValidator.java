package com.example.demo.application.service.school;

import java.time.LocalTime;

import com.example.demo.domain.model.Reservations;

public class KaraokeRuleValidator {

    public boolean isValid(Reservations reservation) {
        // カラオケの特別ルールを判定
        return reservation.getStartTime().isAfter(LocalTime.of(15, 0)) &&
               reservation.getEndTime().isBefore(LocalTime.of(17, 30));
    }
}

