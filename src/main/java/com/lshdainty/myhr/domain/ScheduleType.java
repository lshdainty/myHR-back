package com.lshdainty.myhr.domain;

import lombok.Getter;

@Getter
public enum ScheduleType {
    DAYOFF("DAYOFF", "1"),                      // 연차
    MORNINGOFF("MORNINGOFF", "2"),              // 오전반차
    AFTERNOONOFF("AFTERNOONOFF", "3"),          // 오후반차
    ONETIMEOFF("ONETIMEOFF", "7"),              // 1시간 휴가
    TWOTIMEOFF("TWOTIMEOFF", "8"),              // 2시간 휴가
    THREETIMEOFF("THREETIMEOFF", "9"),          // 3시간 휴가
    FIVETIMEOFF("FIVETIMEOFF", "10"),           // 5시간 휴가
    SIXTIMEOFF("SIXTIMEOFF", "11"),             // 6시간 휴가
    SEVENTIMEOFF("SEVENTIMEOFF", "12"),         // 7시간 휴가
    HALFTIMEOFF("HALFTIMEOFF", "13"),           // 30분 휴가
    EDUCATION("EDUCATION", "6"),                // 교육
    BIRTHDAY("BIRTHDAY", "5"),                  // 생일
    BUSINESSTRIP("BUSINESSTRIP", "4"),          // 출장
    DEFENSE("DEFENSE", "15"),                   // 민방위
    DEFENSEHALF("DEFENSEHALF", "14"),           // 민방위(반차)
    HEALTHCHECK("HEALTHCHECK", "17"),           // 건강검진
    HEALTHCHECKHALF("HEALTHCHECKHALF", "16"),   // 건강검진(반차)
    BIRTHPARTY("BIRTHPARTY", "18");             // 생일파티

    private String newScheduleType;
    private String oldScheduleType;

    ScheduleType(String newScheduleType, String oldScheduleType) {
        this.newScheduleType = newScheduleType;
        this.oldScheduleType = oldScheduleType;
    }
}
