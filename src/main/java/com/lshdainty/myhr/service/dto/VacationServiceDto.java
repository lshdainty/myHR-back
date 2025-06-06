package com.lshdainty.myhr.service.dto;

import com.lshdainty.myhr.domain.Schedule;
import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.domain.VacationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
public class VacationServiceDto {
    private Long id;
    private User user;
    private List<Schedule> schedules;
    private List<ScheduleServiceDto> scheduleDtos;
    private String name;
    private String desc;
    private VacationType type;
    private BigDecimal grantTime;
    private BigDecimal usedTime;
    private BigDecimal remainTime;
    private LocalDateTime occurDate;
    private LocalDateTime expiryDate;
    private String delYN;

    @Override
    public String toString() {
        return "VacationServiceDto{" +
                "id: " + id +
                ", name: '" + name + '\'' +
                ", desc: '" + desc + '\'' +
                ", type: " + type +
                ", grantTime: " + grantTime +
                ", usedTime: " + usedTime +
                ", remainTime: " + remainTime +
                ", occurDate: " + occurDate +
                ", expiryDate: " + expiryDate +
                ", delYN: '" + delYN + '\'' +
                '}';
    }
}
