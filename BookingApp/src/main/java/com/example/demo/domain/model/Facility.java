package com.example.demo.domain.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 施設（学校施設）の情報
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facility {

    private Integer id;
    
    /** 施設名（カラオケ、ジム、ブース など） */
    private String facilityTypeName;

    /** 利用開始時間 */
    private LocalDate startTime;

    /** 利用終了時間 */
    private LocalDate endTime;

    /** 施設の状態（ジムの場合は使用中・空き） */
    private boolean isInUse;  // ジムの各機器の使用状況を管理

    /** 施設が現在アクティブかどうか（利用可能か） */
    private boolean isActive;  // 施設が使用可能かどうか

}
