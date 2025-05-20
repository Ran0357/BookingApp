package com.example.demo.presentation.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.service.api.FacilityAvailabilityRestAppService;

import lombok.RequiredArgsConstructor;

/*
 * サイト空き情報 Rest Controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schedule")
public class FacilityAvailabilityRestController {

	private final FacilityAvailabilityRestAppService facilityAvailabilityRestAppService;
	
	/**
	 * サイト空き状況(スケジュール用)の検索
	 * @param facilityTypeId
	 * @param 
	 * @param 
	 * @return
	 */
	@GetMapping(value = "/facilityTypes/{facilityTypeId}", params = {"start", "end"})
	public List<ResultFacilityAvailability> fetchFacilityAvailabilityForSchedule(
			@PathVariable("facilityTypeId") int facilityTypeId,
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		return facilityAvailabilityRestAppService.fetchFacilityAvailabilityForSchedule(facilityTypeId, startDate, endDate);
	}
	
}
