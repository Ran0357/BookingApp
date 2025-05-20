package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.model.FacilityType;

@Mapper
public interface FacilityTypeMapper {

	/**
	 * サイトタイプ全件取得
	 * @return
	 */
	public List<FacilityType> findAllFacilityType();
	
	/**
	 * サイトタイプ取得(ID指定)
	 * @param siteTypeId サイトタイプID
	 * @return
	 */
	public Optional<FacilityType> findByFacilityTypeId(int facilityTypeId);
}
