package com.example.demo.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.domain.model.FacilityType;
import com.example.demo.repository.FacilityTypeMapper;

import lombok.RequiredArgsConstructor;

/*
 * サイトタイプ Domain Service
 */
@RequiredArgsConstructor
@Service
public class FacilityTypeService {

	private final FacilityTypeMapper facilityTypeMapper;
	
	/**
	 * サイトタイプ全件取得
	 * @return
	 */
	public List<FacilityType> findAllFacilityType() {
		return facilityTypeMapper.findAllFacilityType();
	}
	
	/**
	 * サイトタイプ取得(ID指定)
	 * @param siteTypeId サイトタイプID
	 * @return
	 */
	public Optional<FacilityType> findByFacilityTypeId(int facilityTypeId) {
		return facilityTypeMapper.findByFacilityTypeId(facilityTypeId);
    }
	}

