package com.lshdainty.myhr.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lshdainty.myhr.domain.Vacation;
import com.lshdainty.myhr.domain.VacationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VacationDto {
    private Long vacationId;
    private String vacationName;
    private String vacationDesc;
    private VacationType vacationType;
    private BigDecimal grantTime;
    private LocalDateTime occurDate;
    private LocalDateTime expiryDate;

    private Long userNo;

    public VacationDto(Long vacationId) {
        this.vacationId = vacationId;
    }

    public VacationDto(Vacation vacation) {
        this.vacationId = vacation.getId();
        this.vacationName = vacation.getName();
        this.vacationDesc = vacation.getDesc();
        this.vacationType = vacation.getType();
        this.grantTime = vacation.getGrantTime();
        this.occurDate = vacation.getOccurDate();
        this.expiryDate = vacation.getExpiryDate();
    }
}
