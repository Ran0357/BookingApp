package com.example.demo.application.service;

import java.util.List;

//import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.model.FacilityType;
import com.example.demo.repository.FacilityTypeMapper;

import lombok.RequiredArgsConstructor;

/*
 * トップページ Application Service
 */
@RequiredArgsConstructor
@Service
public class TopAppService {

	private final FacilityTypeMapper facilityTypeRepository;

	/**
	 * 施設タイプ一覧取得
	 * 
	 * @return 施設タイプ一覧
	 */
	public List<FacilityType> getFacilityTypeList() {
		return facilityTypeRepository.findAllFacilityType();
	}
}
