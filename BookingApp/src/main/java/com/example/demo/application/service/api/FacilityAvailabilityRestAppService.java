package com.example.demo.application.service.api;

import static java.time.format.DateTimeFormatter.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.domain.model.FacilityAvailability;
import com.example.demo.domain.service.FacilityAvailabilityService;
import com.example.demo.presentation.api.ResultFacilityAvailability;

import lombok.RequiredArgsConstructor;

/*
 * サイト空き状況（スケジュール用）の検索 Application Service
 */
@RequiredArgsConstructor
@Service
public class FacilityAvailabilityRestAppService {

	private final FacilityAvailabilityService facilityAvailabilityService;

	/**
	 * サイト空き状況(スケジュール用)の検索
	 * @param facilityTypeId
	 * @param startDate 取得開始日
	 * @param endDate 取得終了日
	 * @return
	 */
	public List<ResultFacilityAvailability> fetchFacilityAvailabilityForSchedule(int facilityTypeId, LocalDate startDate,
			LocalDate endDate) {

		// サイトの空き状況検索
		List<FacilityAvailability> result = facilityAvailabilityService.findFacilityAvailability(facilityTypeId,
				startDate, endDate);

		if (result.isEmpty()) {
			System.out.println("Mapper returned: " + (result == null ? "null" : result.size()));
			throw new RuntimeException();
		}

		return result.stream().map(element -> new ResultFacilityAvailability(
				generateTitle(element.getAvailabilityCount(), facilityTypeId),
				element.getDate(),
				generateUrl(element.getAvailabilityCount(), element.getFacilityTypeId(), element.getDate())))
				.collect(Collectors.toList());
	}

	/**
	 * スケジュール(FullCalendar)のイベントタイトル
	 * @param availabilityCount
	 * @return
	 */
	private String generateTitle(int availabilityCount, int facilityTypeId) {

		//施設ごとに表示されるタイトルを変更
		//1:カラオケ、2:ジム、3:ブース
		//カラオケは空きがないと×、1つあれば◎
		//ジムは予約ではない
		//ブースは3つ以上あれば◎、1つあれば△,0なら×

		String title = "";
		if (facilityTypeId == 1) {
			title = availabilityCount == 0 ? "×" : "◎";
		} else if (facilityTypeId == 2) {
			title = "予約なし";
		} else if (facilityTypeId == 3) {
			title = availabilityCount >= 3 ? "◎" : availabilityCount > 0 ? "△" : "×";
		}
		return title;
	}

	private String generateUrl(int availabilityCount, int facilityTypeId, LocalDate useDate) {

		String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/school/facilityInfo")
				.queryParam("facilityId", facilityTypeId)
				.queryParam("useDate", useDate.format(ISO_LOCAL_DATE))
				.toUriString();

		return availabilityCount == 0 ? "" : url;
	}
}
