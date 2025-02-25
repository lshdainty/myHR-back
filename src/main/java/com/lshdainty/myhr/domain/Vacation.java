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
    private String name;

    @Column(name = "vacation_description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "vacation_type")
    private VacationType type;

    @Column(name = "granted_time")
    private float grantedTime;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "del_yn")
    private String delYN;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "create_user_no")
    private Long createUserNo;

    @Column(name = "create_ip")
    private String createIP;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @Column(name = "delete_user_no")
    private Long deleteUserNo;

    @Column(name = "delete_ip")
    private String deleteIP;

    // 휴가 생성자 (setter말고 해당 메소드 사용할 것)
    public static Vacation addVacation(User user, String name, String desc, VacationType type, float granted, LocalDateTime expiratation, Long createUserNo, String createIP) {
        Vacation vacation = new Vacation();
        vacation.user = user;
        vacation.name = name;
        vacation.description = desc;
        vacation.type = type;
        vacation.grantedTime = granted;
        vacation.expirationDate = expiratation;
        vacation.delYN = "N";
        vacation.createUserNo = createUserNo;
        vacation.createIP = createIP;
        vacation.createDate = LocalDateTime.now();
        return vacation;
    }

    // 휴가 삭제 (setter말고 해당 메소드 사용할 것)
    public void deleteVacation(Long deleteUserNo, String clientIP) {
        this.delYN = "N";
        this.deleteDate = LocalDateTime.now();
        this.deleteUserNo = deleteUserNo;
        this.deleteIP = clientIP;
    }
}
