package com.example.demo.presentation.api;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
	
	/** チェックイン日 */
	@JsonSerialize
	private LocalDate start;
	
	/** URL */
	private String url;
}
