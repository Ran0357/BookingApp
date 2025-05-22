package com.example.demo.domain.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.model.FacilityAvailability;
import com.example.demo.repository.FacilityAvailabilityMapper;

import lombok.RequiredArgsConstructor;

/**
 * サイト空き状況 Domain Service
 */
@RequiredArgsConstructor
@Service
public class FacilityAvailabilityService {

    private final FacilityAvailabilityMapper facilityAvailabilityMapper;

    /**
     * サイト空き状況検索（日付範囲指定）
     * @param facilityTypeId サイトタイプID
     * @param from 開始日
     * @param to 終了日
     * @return 空き状況リスト
     */
    public List<FacilityAvailability> findFacilityAvailability(int facilityTypeId, LocalDate from, LocalDate to) {
        return facilityAvailabilityMapper.findFacilityAvailability(facilityTypeId, from, to);
    }

    /**
     * 日単位の空き状況確認（例: カラオケ）
     */
    public boolean isAvailableOnDate(int facilityTypeId, LocalDate date) {
        List<FacilityAvailability> list = facilityAvailabilityMapper.findFacilityAvailability(facilityTypeId, date, date);
        return !list.isEmpty();
    }

    /**
     * 時間帯の空き状況確認（例: ブース、ジム）
     */
    public boolean isAvailableForTimeRange(int facilityTypeId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<FacilityAvailability> list = facilityAvailabilityMapper.findFacilityAvailabilityForTimeRange(facilityTypeId, date, startTime, endTime);
        System.out.println("空き状況ヒット件数: " + list.size());
        return !list.isEmpty();
    }

    /**
     * 空き数を1減らす（在庫減算）
     */
    public int reduceAvailabilityCount(int facilityTypeId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return facilityAvailabilityMapper.reduceAvailabilityCount(facilityTypeId, date, startTime, endTime);
    }
}
