package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.model.FacilityAvailability;

@Mapper
public interface FacilityAvailabilityMapper {

    /**
     * サイト空き状況検索
     * @param facilityTypeId サイトタイプID
     * @param from 取得開始日
     * @param to 取得終了日
     * @return 空き状況リスト
     */
    public List<FacilityAvailability> findFacilityAvailability(int facilityTypeId, LocalDate from, LocalDate to);

    /**
     * サイト空き状況残数
     * @param facilityTypeId サイトタイプID
     * @param from 取得開始日
     * @param to 取得終了日
     * @return 空き状況残数
     */
    public int countDaysNotAvailable(int facilityTypeId, LocalDate from, LocalDate to);

    /**
     * サイト空き状況減算処理
     * @param facilityTypeId サイトタイプID
     * @param date 取得日
     * @param startTime 取得開始時間
     * @param endTime 取得終了時間
     * @return 更新件数
     */
    public int reduceAvailabilityCount(int facilityTypeId, LocalDate date, LocalTime startTime, LocalTime endTime);

    /**
     * 時間単位の空き状況確認
     * @param facilityTypeId サイトタイプID
     * @param date 取得日
     * @param startTime 取得開始時間
     * @param endTime 取得終了時間
     * @return 空き状況リスト
     */
    List<FacilityAvailability> findFacilityAvailabilityForTimeRange(int facilityTypeId, LocalDate date, LocalTime startTime, LocalTime endTime);
    
	/**
	 * 空き状況の復元処理
	 * @param facilityTypeId サイトタイプID
	 * @param date 取得日
	 * @param startTime 取得開始時間
	 * @param endTime 取得終了時間
	 * @return 更新件数
	 */
    int restoreAvailabilityCount(Integer facilityTypeId, LocalDate date, LocalTime startTime, LocalTime endTime);

}
