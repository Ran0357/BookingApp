package com.example.demo.presentation.school;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FacilityUseForm {

    /** 施設ID */
    @NotNull
    private int facilityId;

    /** 利用日 */
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate useDate;

    /** 開始時間 */
    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private java.time.LocalTime startTime;

    /** 終了時間 */
    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private java.time.LocalTime endTime;

    /** 利用人数 */
    @NotNull
    @Min(1)
    private int numberOfPeople;

    /** 施設名（画面表示用） */
    @NotBlank
    private String facilityName;

	

}

