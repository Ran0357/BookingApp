package com.example.demo.domain.service;

import java.time.LocalTime;

import org.springframework.stereotype.Service;

import com.example.demo.domain.model.Reservations;

@Service
public class KaraokeRuleValidator {

    public boolean isValid(Reservations reservation) {
        // カラオケの特別ルールを判定
    	return !reservation.getStartTime().isBefore(LocalTime.of(15, 0)) &&
    	           !reservation.getEndTime().isAfter(LocalTime.of(17, 30));
    }
}
