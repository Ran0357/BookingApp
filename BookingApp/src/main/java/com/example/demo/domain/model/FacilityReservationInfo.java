package com.example.demo.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class FacilityReservationInfo {
	
    
    /** 施設ID */
    private int facilityId;

    /** 利用日 */
    private LocalDate useDate;

    /** 利用開始時間 */
    private LocalTime startTime;

    /** 利用終了時間 */
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
	
	
    

}

