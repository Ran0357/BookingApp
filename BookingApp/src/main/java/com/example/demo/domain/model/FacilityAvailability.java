package com.example.demo.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

/*
 * サイト空き状況（時間帯付き）
 */
@Data
public class FacilityAvailability {

	/** 日にち */
	private LocalDate date;

	/** サイトID（または施設ID） */
	private int facilityTypeId;;

	/** 利用開始時間 */
	private LocalTime startTime;

	/** 利用終了時間 */
	private LocalTime endTime;

	/** 空き数 */
	private int availabilityCount;

	/** 最大数 */
	private int maxCount;
}
