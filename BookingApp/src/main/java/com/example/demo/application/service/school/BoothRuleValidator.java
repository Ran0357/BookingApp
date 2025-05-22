package com.example.demo.application.service.school;

import org.springframework.stereotype.Component;

import com.example.demo.domain.model.Reservations;

@Component
public class BoothRuleValidator {
	public boolean isValid(Reservations reservation) {
		// ブースの特別ルールを判定
		// 予約人数が1より大きい場合は、エラー
		if (reservation.getNumberOfPeople() > 1) {
			return false;
		}
		return reservation.getNumberOfPeople() == 1;

	}
}
