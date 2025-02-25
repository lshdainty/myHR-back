package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
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
    private ScheduleType scheduleType;

    @Column(name = "schedule_desc")
    private String schduleDesc;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "del_yn")
    private String delYN;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "create_user_no")
    private String createUserNo;

    @Column(name = "create_ip")
    private String createIP;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @Column(name = "delete_user_no")
    private String deleteUserNo;

    @Column(name = "delete_ip")
    private String deleteIP;
}
