package com.example.demo.presentation.api;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * サイト空き状況(スケジュール用)
 */
@Data
@AllArgsConstructor
public class ResultFacilityAvailability {

	/** タイトル */
	private String title;
	
	/** 予約日+ 予約開始時間 */
	private LocalDateTime start;
	
	/** 予約日+ 予約終了時間 */
	private LocalDateTime end;
	
	/** URL */
	private String url;
}
