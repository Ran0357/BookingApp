package com.example.demo.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.demo.presentation.school.FacilityUseForm;

import lombok.Data;

@Data
public class FacilityReservationInfo {
	
    
    /** 施設ID */
    private int facilityId;

    /** 利用日 */
    private LocalDate useDate;

    /** 利用開始時間 */
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    /** 利用終了時間 */
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    /** 利用人数 */
    private int numberOfPeople;

    /** 利用目的（任意） */
    private String purpose;

    /**
	 * 人数上限判定
	 * 引数で渡されるサイトの上限人数を上回る場合はfalse, それ以外はtrue
	 * @param upperLimit サイトの上限人数
	 * @return 判定値
	 */
	public boolean isValidNumberOfPeople(int upperLimit) {
		return numberOfPeople <= upperLimit;
	}
	
	public static FacilityReservationInfo from(FacilityUseForm form) {
	    if (form == null) return null;

	    FacilityReservationInfo info = new FacilityReservationInfo();
	    info.setFacilityId(form.getFacilityId());
	    info.setUseDate(form.getUseDate());
	    info.setStartTime(form.getStartTimeOnly());  // LocalTime
	    info.setEndTime(form.getEndTimeOnly());      // LocalTime
	    info.setNumberOfPeople(form.getNumberOfPeople());
	    info.setPurpose(null); // フォームに無いので null にする or 必要なら form に追加

	    return info;
	}

}

