package com.lshdainty.myhr.domain;

public enum ScheduleType {
    DAYOFF,         // 연차
    MORNINGOFF,     // 오전반차
    AFTERNOONOFF,   // 오후반차
    ONETIMEOFF,     // 1시간 휴가
    TWOTIMEOFF,     // 2시간 휴가
    THREETIMEOFF,   // 3시간 휴가
    FIVETIMEOFF,    // 5시간 휴가
    SIXTIMEOFF,     // 6시간 휴가
    SEVENTIMEOFF,   // 7시간 휴가
    HALFTIMEOFF,    // 30분 휴가
    EDUCATION,      // 교육
    BIRTHDAY,       // 생일
    BUSINESSTRIP,   // 출장
    DEFENSE,        // 민방위
    DEFENSEHALF,        // 민방위(반차)
    HEALTHCHECK,        // 건강검진
    HEALTHCHECKHALF,    // 건강검진(반차)
    BIRTHPARTY          // 생일파티
}
