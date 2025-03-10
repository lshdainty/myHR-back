package com.lshdainty.myhr.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum ScheduleType {
    DAYOFF("1", dayDiff -> dayDiff.multiply(BigDecimal.valueOf(8))),           // 연차
    MORNINGOFF("2", dayDiff -> dayDiff.multiply(BigDecimal.valueOf(4))),       // 오전반차
    AFTERNOONOFF("3", dayDiff -> dayDiff.multiply(BigDecimal.valueOf(4))),     // 오후반차
    ONETIMEOFF("7", dayDiff -> dayDiff.multiply(BigDecimal.valueOf(1))),       // 1시간 휴가
    TWOTIMEOFF("8", dayDiff -> dayDiff.multiply(BigDecimal.valueOf(2))),       // 2시간 휴가
    THREETIMEOFF("9", dayDiff -> dayDiff.multiply(BigDecimal.valueOf(3))),     // 3시간 휴가
    FIVETIMEOFF("10", dayDiff -> dayDiff.multiply(BigDecimal.valueOf(5))),     // 5시간 휴가
    SIXTIMEOFF("11", dayDiff -> dayDiff.multiply(BigDecimal.valueOf(6))),      // 6시간 휴가
    SEVENTIMEOFF("12", dayDiff -> dayDiff.multiply(BigDecimal.valueOf(7))),    // 7시간 휴가
    HALFTIMEOFF("13", dayDiff -> dayDiff.multiply(BigDecimal.valueOf(0.5))),   // 30분 휴가
    EDUCATION("6"),                                                                         // 교육
    BIRTHDAY("5"),                                                                          // 생일
    BUSINESSTRIP("4"),                                                                      // 출장
    DEFENSE("15"),                                                                          // 민방위
    DEFENSEHALF("14"),                                                                      // 민방위(반차)
    HEALTHCHECK("17"),                                                                      // 건강검진
    HEALTHCHECKHALF("16"),                                                                  // 건강검진(반차)
    BIRTHPARTY("18");                                                                       // 생일파티

    private String oldType;
    private Function<BigDecimal, BigDecimal> expression;

    ScheduleType(String oldType, Function<BigDecimal, BigDecimal> expression) {
        this.oldType = oldType;
        this.expression = expression;
    }

    ScheduleType(String oldType) {
        this.oldType = oldType;
    }

    public BigDecimal convertToValue(int dayCount) {
        return expression.apply(BigDecimal.valueOf(dayCount));
    }
}
