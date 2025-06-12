package com.example.demo.presentation.school;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDateTime;

    /** 終了時間 */
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDateTime;

    /** 利用人数 */
    @NotNull
    @Min(1)
    private int numberOfPeople;

    /** 施設名（画面表示用） */
    @NotBlank
    private String facilityName;
    
    @DateTimeFormat(pattern = "HH:mm")
    public LocalTime getStartTimeOnly() {
        return startDateTime != null ? startDateTime.toLocalTime() : null;
    }

    @DateTimeFormat(pattern = "HH:mm")
    public LocalTime getEndTimeOnly() {
        return endDateTime != null ? endDateTime.toLocalTime() : null;
    }
    
    @DateTimeFormat(pattern = "HH:mm")
    public void setStartTimeOnly(LocalTime time) {
        if (time != null) {
            if (this.startDateTime == null) {
                this.startDateTime = LocalDateTime.of(this.useDate != null ? this.useDate : LocalDate.now(), time);
            } else {
                this.startDateTime = LocalDateTime.of(this.startDateTime.toLocalDate(), time);
            }
        }
    }

    @DateTimeFormat(pattern = "HH:mm")
    public void setEndTimeOnly(LocalTime time) {
        if (time != null) {
            if (this.endDateTime == null) {
                this.endDateTime = LocalDateTime.of(this.useDate != null ? this.useDate : LocalDate.now(), time);
            } else {
                this.endDateTime = LocalDateTime.of(this.endDateTime.toLocalDate(), time);
            }
        }
    }

    
    
    
    
}

	



