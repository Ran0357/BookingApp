package com.example.demo.application.service.school;

import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.example.demo.domain.model.Reservations;

@Component
public class KaraokeRuleValidator {

	public boolean isValid(Reservations reservation) {
    	//利用人数が4人以下の場合は、エラー
    	if (reservation.getNumberOfPeople() <= 4) {
    		return false;
    	}
    	
        // カラオケの特別ルールを判定
    	return !reservation.getStartTime().isBefore(LocalTime.of(15, 30)) &&
    	           !reservation.getEndTime().isAfter(LocalTime.of(17, 30));
    }
}

