package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // -> protected Order() {}와 동일한 의미 (롬복으로 생성자 막기)
@Table(name = "deptop_holiday")
public class Holiday {
    @Id @GeneratedValue
    @Column(name = "holiday_seq")
    private Long seq;

    @Column(name = "holiday_name")
    private String name;

    @Column(name = "holiday_date")
    @NotNull
    private String date;

    // 휴일 생성자 (setter말고 해당 메소드 사용할 것)
    public static Holiday createHoliday(String name, String date) {
        Holiday holiday = new Holiday();
        holiday.name = name;
        holiday.date = date;
        return holiday;
    }

    // 휴일 수정 (setter말고 해당 메소드 사용할 것)
    public void updateHoliday(String name, String date) {
        if (!Objects.isNull(name)) { this.name = name; }
        if (!Objects.isNull(date)) { this.date = date; }
    }
}
