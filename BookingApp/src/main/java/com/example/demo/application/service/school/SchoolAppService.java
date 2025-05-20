package com.example.demo.application.service.school;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.FacilityType;
import com.example.demo.domain.service.FacilityTypeService;
import com.example.demo.exception.SystemException;

import lombok.RequiredArgsConstructor;

/*
 * 施設情報表示 Application Service
 */
@RequiredArgsConstructor
@Service
public class SchoolAppService {

	private final FacilityTypeService facilityTypeService;
	private final MessageSource messageSource;
	
	/**
	 * サイトタイプ全件取得
	 * @return
	 */
	public List<FacilityType> findAllFacilityType() {
		return facilityTypeService.findAllFacilityType();
	}
	
	/**
	 * サイトタイプ名取得
	 * @param siteTypeId サイトタイプID
	 * @return
	 */
	public String findFacilityTypeName(int facilityTypeId) {
		
		return facilityTypeService.findByFacilityTypeId(facilityTypeId)
				.map(st -> st.getName())
				.orElseThrow(() -> new SystemException(messageSource.getMessage("exception.dataNotFound", new String[] {String.valueOf(facilityTypeId)}, Locale.JAPAN)));
	}
}
