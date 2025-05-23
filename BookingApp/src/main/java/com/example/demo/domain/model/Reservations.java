package com.example.demo.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 予約管理
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservations {

	/** 予約ID */
	private Integer id;

	/** サイトタイプID */
	private Integer facilityTypeId;

	/** 予約日 */
	private LocalDate reservationDate;

	/** 予約開始時間 */
	private LocalTime startTime;

	/** 予約終了時間 */
	private LocalTime endTime;

	/** 予約人数 */
	private Integer numberOfPeople;

	/** 会員ID */
	private Integer memberId;

	/** キャンセル日 */
	private LocalDateTime canceledAt;

	/** 作成日 */
	private LocalDateTime createdAt;

	/** Memberの情報 */
	private Member member;
	

	private FacilityType facilityType;

	/**
	 * キャンセル済み判定
	 * キャンセル済みの場合true, それ以外はfalse
	 * @return
	 */
	public boolean isCanceled() {
		return canceledAt != null;
	}
	/**
     * キャンセル可能かどうかを判定するメソッド
     * 現時点では、キャンセルされていなければいつでもキャンセル可能
     */
    public boolean canCancel() {
    	 return !isCanceled() && !reservationDate.isBefore(LocalDate.now());
    }

	/**
	* ジムの場合、使用機器の状態や空き状況をチェックするためのメソッド
	* 現状ではカラオケ部屋やジム機器の予約の確認に使用される
	*/
	public boolean isGymReservation() {
		return facilityTypeId != null && facilityTypeId == 2; // ジム施設に関連する予約の場合
	}

	 /**
     * カラオケの予約かどうかを判定するメソッド
     * 例: カラオケのサイトタイプIDが1の場合
     * @return
     */
    public boolean isKaraokeReservation() {
        return facilityTypeId != null && facilityTypeId == 1; // サイトタイプIDが1の時、カラオケ予約と判定
    }
    
	/**
	 * ブースの予約かどうかを判定するメソッド
	 * 例: ブースのサイトタイプIDが3の場合
	 * @return 
	 */
     public boolean isBoothReservation() {
    	 return facilityTypeId != null && facilityTypeId == 3; // サイトタイプIDが3の時、ブース予約と判定
     }




}
