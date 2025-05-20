package com.example.demo.presentation.school;

import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.domain.model.FacilityReservationInfo;
import com.example.demo.domain.model.FacilityType;
import com.example.demo.domain.service.FacilityAvailabilityService;
import com.example.demo.domain.service.FacilityTypeService;
import com.example.demo.exception.SystemException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FacilityUseFormValidator implements Validator {

	private final FacilityTypeService facilityTypeService;
	private final FacilityAvailabilityService facilityAvailabilityService;
	private final ModelMapper modelMapper;
	private final MessageSource messageSource;

	@Override
	public boolean supports(Class<?> clazz) {
		return FacilityUseForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		FacilityUseForm facilityUseForm = (FacilityUseForm) target;
		FacilityReservationInfo reservationInfo = modelMapper.map(facilityUseForm, FacilityReservationInfo.class);

		// 上限人数取得（facilityId か siteTypeId どちらを使うかは設計により選択）
		int capacity = facilityTypeService.findByFacilityTypeId(reservationInfo.getFacilityId())
				.map(site -> site.getCapacity())
				.orElseThrow(() -> new SystemException(
						messageSource.getMessage("exception.dataNotFound",
								new String[] { String.valueOf(reservationInfo.getFacilityId()) },
								Locale.JAPAN)));

		// 利用人数検証
		validateNumberOfPeople(errors, reservationInfo, capacity);

		// 空き状況検証
		validateSiteAvailability(errors, reservationInfo);
	}



	/**
	 * 利用人数検証
	 * 利用人数がサイトの上限を上回る場合エラー
	 * @param errors
	 * @param facilityUseForm
	 * @param capacity サイトの上限人数
	 */
	private void validateNumberOfPeople(Errors errors,FacilityReservationInfo facilityUseInfo , int capacity) {
		
		if (!facilityUseInfo.isValidNumberOfPeople(capacity)) {
			errors.rejectValue("numberOfPeople", "validation.custom.numberOfPeopleIncorrect");
		}
	}
	
	
	/**
	 * サイトの空き状況検証
	 * チェックイン日からチェックアウト日までの期間で、満室の日程がある場合はエラー
	 * @param errors
	 * @param facilityUseForm 
	 * @param facilityUseInfo 施設情報
	 */
	private void validateSiteAvailability(Errors errors, FacilityReservationInfo info) {
		FacilityType facilityType = facilityTypeService.findByFacilityTypeId(info.getFacilityId())
			.orElseThrow(() -> new SystemException("施設情報が見つかりません"));

		int facilityTypeId = facilityType.getId(); // 施設タイプIDで分岐

		boolean available;

		if (facilityTypeId == 1) {
			// カラオケ（日単位）
			available = facilityAvailabilityService.isAvailableOnDate(
				info.getFacilityId(), info.getUseDate());
		} else {
			// ジム・ブース（時間単位）
			available = facilityAvailabilityService.isAvailableForTimeRange(
				info.getFacilityId(), info.getUseDate(), info.getStartTime(), info.getEndTime());
		}

		if (!available) {
			errors.rejectValue("useDate", "validation.custom.siteAvailabilityIncorrect");
		}
	}

}