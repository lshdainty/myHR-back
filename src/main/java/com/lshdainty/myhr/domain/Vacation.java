package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // -> protected Order() {}와 동일한 의미 (롬복으로 생성자 막기)
@Table(name = "deptop_vacation")
public class Vacation extends AuditingFields {
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

    @Column(name = "occur_date")
    private LocalDateTime occurDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "del_yn")
    private String delYN;

    // 휴가 생성자 (setter말고 해당 메소드 사용할 것)
    public static Vacation addVacation(User user, String name, String desc, VacationType type, float grantTime, LocalDateTime occurDate, LocalDateTime expiryDate, Long userNo, String clientIP) {
        Vacation vacation = new Vacation();
        vacation.user = user;
        vacation.name = name;
        vacation.desc = desc;
        vacation.type = type;
        vacation.grantTime = grantTime;
        vacation.occurDate = occurDate;
        vacation.expiryDate = expiryDate;
        vacation.delYN = "N";
        vacation.setCreated(userNo, clientIP);
        return vacation;
    }

    // 휴가 삭제 (setter말고 해당 메소드 사용할 것)
    public void deleteVacation(Long userNo, String clientIP) {
        this.delYN = "Y";
        this.setDeleted(LocalDateTime.now(), userNo, clientIP);
    }
}
