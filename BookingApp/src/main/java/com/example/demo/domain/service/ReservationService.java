package com.example.demo.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.model.Reservations;
import com.example.demo.repository.ReservationMapper;

import lombok.RequiredArgsConstructor;

/*
 * 予約 Domain Service
 */
@RequiredArgsConstructor
@Transactional
@Service
public class ReservationService {
	
	private final ReservationMapper reservationMapper;

	/**
	 * 予約登録
	 * @param reservation 予約
	 * @return
	 */
	public int createReservation(Reservations reservation) {
		return reservationMapper.createReservation(reservation);
	}
	
	/**
	 * 予約検索(ページネーション)
	 * @param memberId 会員ID
	 * @param pageable ページ情報
	 * @return
	 */
	public Page<Reservations> searchReservations(int memberId, Pageable pageable) {
		
		// 指定した会員の予約情報取得
		List<Reservations> reservationList = reservationMapper.findPageByMemberId(memberId, pageable);
		// 指定した会員の予約数取得
		int count = reservationMapper.countByMemberId(memberId);
		
		return new PageImpl<>(reservationList, pageable, count);
	}
	
	/**
	 * 予約キャンセル
	 * @param reservationId 予約ID
	 * @return
	 */
	public int cancelReservation(int reservationId) {
		return reservationMapper.cancelReservation(reservationId);
	}
	

	/**
	 * 予約情報取得
	 * @param reservationId 予約ID
	 * @return
	 */
	public boolean existsByDateAndFacilityType(LocalDate reservationDate, Integer facilityTypeId) {
		// 予約日とサイトタイプIDで予約が存在するか確認
		List<Reservations> reservations = reservationMapper.findByDateAndFacilityType(reservationDate, facilityTypeId);
		// 予約が存在する場合はtrueを返す
		if (reservations != null && !reservations.isEmpty()) {
			return true;
		}
		// 予約が存在しない場合はfalseを返す
		return false;
	}

	/**
	 * 予約詳細取得
	 * @param reservationId 予約ID
	 * @return
	 */
	public Optional<Reservations> findReservationDetailsById(int reservationId) {
		return reservationMapper.findReservationDetailsById(reservationId);
		
	}
	
}
