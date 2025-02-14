package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "deptop_vacation")
public class Vacation {
    @Id @GeneratedValue
    @Column(name = "vacation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "vacation_type")
    private VacationType type;

    @Column(name = "vacation_detail")
    private String detail;

    @Column(name = "granted_time")
    private float grantedTime;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

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
