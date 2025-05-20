package com.example.demo.application.service.member;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.Reservations;
import com.example.demo.domain.service.ReservationService;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.SystemException;

import lombok.RequiredArgsConstructor;

/*
 * 予約内容の確認・キャンセル Application Service
 */
@RequiredArgsConstructor
@Service
public class ReservationsAppService {

	private final ReservationService reservationService;
	private final MessageSource messageSource;
	
	/**
	 * 予約検索(ページネーション)
	 * @param memberId 会員ID
	 * @param pageable
	 * @return
	 */
	public Page<Reservations> searchReservations(int memberId, Pageable pageable) {
		return reservationService.searchReservations(memberId, pageable);
	}
	
	/**
	 * 予約詳細取得
	 * @param reservationId 予約ID
	 * @return
	 */
	public Reservations findReservationDetailsById(int reservationId) {
		
		return reservationService.findReservationDetailsById(reservationId)
				.orElseThrow(() -> new SystemException(messageSource.getMessage("exception.dataNotFound", new String[] {String.valueOf(reservationId)}, Locale.JAPAN)));
	}
	
	/**
	 * 予約キャンセル
	 * @param reservationId 予約ID
	 * @return
	 */
	public int cancelReservation(int reservationId) {
		
		// 権限確認
		Reservations reservation = this.findReservationDetailsById(reservationId);
		
		// キャンセル済みはエラー
		if (reservation.isCanceled()) {
			throw new BusinessException(messageSource.getMessage("exception.cannotCancel", null, Locale.JAPAN));
		}
		
		// 予約キャンセル
		return reservationService.cancelReservation(reservationId);
	}
}
