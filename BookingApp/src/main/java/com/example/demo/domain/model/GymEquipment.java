package com.example.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * ジムの機器情報
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GymEquipment {

    private Integer id; // 機器ID
    private Integer siteId; // 施設ID（ジムに関連）
    private String equipmentName; // 機器名（エアロバイク、ランニングマシンなど）
    private boolean isInUse; // 機器が使用中かどうか
}
