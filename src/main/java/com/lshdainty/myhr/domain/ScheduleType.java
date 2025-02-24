package com.lshdainty.myhr.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleType {
    DAYOFF("1"),            // 연차
    MORNINGOFF("2"),        // 오전반차
    AFTERNOONOFF("3"),      // 오후반차
    ONETIMEOFF("7"),        // 1시간 휴가
    TWOTIMEOFF("8"),        // 2시간 휴가
    THREETIMEOFF("9"),      // 3시간 휴가
    FIVETIMEOFF("10"),      // 5시간 휴가
    SIXTIMEOFF("11"),       // 6시간 휴가
    SEVENTIMEOFF("12"),     // 7시간 휴가
    HALFTIMEOFF("13"),      // 30분 휴가
    EDUCATION("6"),         // 교육
    BIRTHDAY("5"),          // 생일
    BUSINESSTRIP("4"),      // 출장
    DEFENSE("15"),          // 민방위
    DEFENSEHALF("14"),      // 민방위(반차)
    HEALTHCHECK("17"),      // 건강검진
    HEALTHCHECKHALF("16"),  // 건강검진(반차)
    BIRTHPARTY("18");       // 생일파티

    private String oldType;

    ScheduleType(String oldType) {
        this.oldType = oldType;
    }
}
