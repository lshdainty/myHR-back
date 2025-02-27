package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // -> protected Order() {}와 동일한 의미 (롬복으로 생성자 막기)
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

    @Column(name = "vacation_desc")
    private String desc;

    @Enumerated(EnumType.STRING)
    @Column(name = "vacation_type")
    private VacationType type;

    @Column(name = "grant_time")
    private float grantTime;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

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

    // 휴가 생성자 (setter말고 해당 메소드 사용할 것)
    public static Vacation addVacation(User user, String name, String desc, VacationType type, float grantTime, LocalDateTime expiryDate, Long userNo, String clientIP) {
        Vacation vacation = new Vacation();
        vacation.user = user;
        vacation.name = name;
        vacation.desc = desc;
        vacation.type = type;
        vacation.grantTime = grantTime;
        vacation.expiryDate = expiryDate;
        vacation.delYN = "N";
        vacation.addUserNo = userNo;
        vacation.addClientIP = clientIP;
        vacation.addDate = LocalDateTime.now();
        return vacation;
    }

    // 휴가 삭제 (setter말고 해당 메소드 사용할 것)
    public void deleteVacation(Long userNo, String clientIP) {
        this.delYN = "Y";
        this.delUserNo = userNo;
        this.delClientIP = clientIP;
        this.delDate = LocalDateTime.now();
    }
}
