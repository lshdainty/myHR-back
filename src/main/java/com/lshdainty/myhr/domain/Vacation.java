package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @Setter
    private User user;

    @Column(name = "vacation_name")
    @Setter
    private String name;

    @Column(name = "vacation_description")
    @Setter
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "vacation_type")
    @Setter
    private VacationType type;

    @Column(name = "granted_time")
    @Setter
    private float grantedTime;

    @Column(name = "expiration_date")
    @Setter
    private LocalDateTime expirationDate;

    @Column(name = "del_yn")
    @Setter
    private String delYN;

    @Column(name = "create_date")
    @Setter
    private LocalDateTime createDate;

    @Column(name = "create_user_no")
    @Setter
    private String createUserNo;

    @Column(name = "create_ip")
    @Setter
    private String createIP;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @Column(name = "delete_user_no")
    private String deleteUserNo;

    @Column(name = "delete_ip")
    private String deleteIP;
}
