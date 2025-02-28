package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // -> protected Order() {}와 동일한 의미 (롬복으로 생성자 막기)
@Table(name = "deptop_schedule")
public class Schedule extends AuditingFields {
    @Id
    @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacation_id")
    private Vacation vacation;

    @Column(name = "schedule_type")
    @Enumerated(EnumType.STRING)
    private ScheduleType type;

    @Column(name = "schedule_desc")
    private String desc;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "del_yn")
    private String delYN;

    // 스케줄 생성자 (setter말고 해당 메소드 사용할 것)
    public static Schedule addSchedule(User user, Vacation vacation, String desc, ScheduleType type, LocalDateTime startDate, LocalDateTime endDate, Long userNo, String clientIP) {
        Schedule schedule = new Schedule();
        schedule.user = user;
        schedule.vacation = vacation;
        schedule.desc = desc;
        schedule.type = type;
        schedule.startDate = startDate;
        schedule.endDate = endDate;
        schedule.delYN = "N";
        schedule.setCreated(LocalDateTime.now(), userNo, clientIP);
        return schedule;
    }

    // 스케줄 삭제 (setter말고 해당 메소드 사용할 것)
    public void deleteSchedule(Long userNo, String clientIP) {
        this.delYN = "Y";
        this.setDeleted(LocalDateTime.now(), userNo, clientIP);
    }
}
