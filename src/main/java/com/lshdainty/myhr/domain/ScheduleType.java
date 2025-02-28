package com.lshdainty.myhr.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum ScheduleType {
    DAYOFF("1", dayDiff -> dayDiff * 8f),           // 연차
    MORNINGOFF("2", dayDiff -> dayDiff * 4f),       // 오전반차
    AFTERNOONOFF("3", dayDiff -> dayDiff * 4f),     // 오후반차
    ONETIMEOFF("7", dayDiff -> dayDiff * 1f),       // 1시간 휴가
    TWOTIMEOFF("8", dayDiff -> dayDiff * 2f),       // 2시간 휴가
    THREETIMEOFF("9", dayDiff -> dayDiff * 3f),     // 3시간 휴가
    FIVETIMEOFF("10", dayDiff -> dayDiff * 5f),     // 5시간 휴가
    SIXTIMEOFF("11", dayDiff -> dayDiff * 6f),      // 6시간 휴가
    SEVENTIMEOFF("12", dayDiff -> dayDiff * 7f),    // 7시간 휴가
    HALFTIMEOFF("13", dayDiff -> dayDiff * 0.5f),   // 30분 휴가
    EDUCATION("6"),                                          // 교육
    BIRTHDAY("5"),                                           // 생일
    BUSINESSTRIP("4"),                                       // 출장
    DEFENSE("15"),                                           // 민방위
    DEFENSEHALF("14"),                                       // 민방위(반차)
    HEALTHCHECK("17"),                                       // 건강검진
    HEALTHCHECKHALF("16"),                                   // 건강검진(반차)
    BIRTHPARTY("18");                                        // 생일파티

    private String oldType;
    private Function<Integer, Float> expression;

    ScheduleType(String oldType, Function<Integer, Float> expression) {
        this.oldType = oldType;
        this.expression = expression;
    }

    ScheduleType(String oldType) {
        this.oldType = oldType;
    }

    public float calculate(LocalDateTime start, LocalDateTime end) {
        return expression.apply(Period.between(start.toLocalDate(), end.toLocalDate()).getDays() + 1);
    }
}
