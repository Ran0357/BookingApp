package com.example.demo.application.service.school;

import java.time.LocalTime;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.model.FacilityReservationInfo;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.Reservations;
import com.example.demo.domain.model.UserInfo;
import com.example.demo.domain.service.FacilityAvailabilityService;
import com.example.demo.domain.service.HolidayService;
import com.example.demo.domain.service.MemberService;
import com.example.demo.domain.service.ReservationService;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.SystemException;

import lombok.RequiredArgsConstructor;

/*
 * 施設予約 Application Service
 */
@RequiredArgsConstructor
@Transactional
@Service
public class ReserveAppService {

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final FacilityAvailabilityService facilityAvailabilityService;
    private final MessageSource messageSource;
    private final HolidayService holidayService;  // 学校休館日判定
    private final KaraokeRuleValidator karaokeRuleValidator;  // カラオケ予約ルールのチェック
    private final BoothRuleValidator boothRuleValidator;  // ブース予約ルールのチェック;

    /**
     * 会員情報取得（ID指定）
     * @param memberId 会員ID
     * @return Member
     */
    public Member findMemberById(int memberId) {
        return memberService.findById(memberId)
                .orElseThrow(() -> new SystemException(
                        messageSource.getMessage("exception.dataNotFound",
                                new String[]{String.valueOf(memberId)},
                                Locale.JAPAN)));
    }

    /**
     * 予約情報組み立て（カラオケの場合、時間は自動設定）
     * @param facilityInfo 施設情報
     * @param userInfo 会員情報
     * @return Reservation
     */
    public Reservations buildReservation(FacilityReservationInfo facilityInfo, UserInfo userInfo) {
        Reservations reservation = new Reservations();
        reservation.setFacilityTypeId(facilityInfo.getFacilityId());
        reservation.setReservationDate(facilityInfo.getUseDate());
        reservation.setNumberOfPeople(facilityInfo.getNumberOfPeople());
        reservation.setMemberId(userInfo.getId());

        // カラオケの場合、開始時間と終了時間を自動設定
        if (facilityInfo.getFacilityId() == 1) { // 施設IDがカラオケ施設の場合
            reservation.setStartTime(LocalTime.of(15, 30)); // 開始時間は15:30
            reservation.setEndTime(LocalTime.of(17, 30));  // 終了時間は17:30
        } else {
            reservation.setStartTime(facilityInfo.getStartTime());
            reservation.setEndTime(facilityInfo.getEndTime());
        }

        return reservation;
    }

    /**
     * 予約登録処理
     * サイト空き状況の在庫を減らし、予約調整を行う
     * @param reservation 予約情報
     */
    public void saveReservation(Reservations reservation) {

        // 施設ごとの予約ルール判定（カラオケの場合）
        if (reservation.isKaraokeReservation()) {
            // カラオケの予約ルールチェック
            if (!karaokeRuleValidator.isValid(reservation)) {
                throw new BusinessException(
                        messageSource.getMessage("exception.reservation.invalidKaraokeRule",
                                null, Locale.JAPAN));
            }

            // 1日1組予約ルール（カラオケの場合）
            boolean alreadyReserved = reservationService.existsByDateAndFacilityType(
                    reservation.getReservationDate(), reservation.getFacilityTypeId());

            if (alreadyReserved) {
                throw new BusinessException(
                        messageSource.getMessage("exception.reservation.karaokeAlreadyReserved", null, Locale.JAPAN));
            }
        }
		//施設ごとに予約ルール判定(ブースの場合)
		else if (reservation.isBoothReservation()) {
			// ブースの予約ルールチェック
			if (!boothRuleValidator.isValid(reservation)) {
				throw new BusinessException(
						messageSource.getMessage("exception.reservation.invalidBoothRule",
								null, Locale.JAPAN));
			}
			
		}

        // 学校休館日チェック
        if (holidayService.isFacilityClosed(
                reservation.getFacilityTypeId(), reservation.getReservationDate())) {
            throw new BusinessException(
                messageSource.getMessage("exception.reservation.facilityHoliday", null, Locale.JAPAN));
        }

        // 空き状況の在庫減少チェック
        int updated = facilityAvailabilityService.reduceAvailabilityCount(
                reservation.getFacilityTypeId(),
                reservation.getReservationDate(),
                reservation.getStartTime(),
                reservation.getEndTime());

        // 在庫が無い場合はエラー
        if (updated == 0) {
            throw new BusinessException(
                    messageSource.getMessage("exception.reservation.noAvailability",
                            new String[]{String.valueOf(reservation.getFacilityTypeId())},
                            Locale.JAPAN));
        }

        // 予約登録
        reservationService.createReservation(reservation);

        // 登録後の予約情報を取得（確認やレスポンス用に）
        reservationService.findReservationDetailsById(reservation.getId())
                .orElseThrow(() -> new SystemException(
                        messageSource.getMessage("exception.dataNotFound",
                                new String[]{String.valueOf(reservation.getId())},
                                Locale.JAPAN)));
    }

}
