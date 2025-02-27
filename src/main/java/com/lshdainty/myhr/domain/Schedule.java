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
public class Schedule {
    @Id
    @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

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

    @Column(name = "add_date")
    private LocalDateTime addDate;

    @Column(name = "add_user_no")
    private Long addUserNo;

    @Column(name = "add_client_ip")
    private String addClientIP;

    @Column(name = "del_date")
    private LocalDateTime delDate;

    @Column(name = "del_user_no")
    private Long delUserNo;

    @Column(name = "del_client_ip")
    private String delClientIP;

    // 스케줄 생성자 (setter말고 해당 메소드 사용할 것)
    public static Schedule addSchedule(User user, String desc, ScheduleType type, LocalDateTime startDate, LocalDateTime endDate, Long userNo, String clientIP) {
        Schedule schedule = new Schedule();
        schedule.user = user;
        schedule.desc = desc;
        schedule.type = type;
        schedule.startDate = startDate;
        schedule.endDate = endDate;
        schedule.delYN = "N";
        schedule.addUserNo = userNo;
        schedule.addClientIP = clientIP;
        schedule.addDate = LocalDateTime.now();
        return schedule;
    }

    // 스케줄 삭제 (setter말고 해당 메소드 사용할 것)
    public void deleteSchedule(Long userNo, String clientIP) {
        this.delYN = "Y";
        this.delUserNo = userNo;
        this.delClientIP = clientIP;
        this.delDate = LocalDateTime.now();
    }
}
