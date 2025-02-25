package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Table(name = "deptop_user")
public class User {
    @Id @GeneratedValue
    @Column(name = "user_no")
    private Long id;

    @Column(name = "user_name")
    @Setter
    private String name;

    @Column(name = "user_pwd")
    private String pwd;

    @Column(name = "user_ip")
    private String ip;

    @Column(name = "user_role")
    private int role;

    @Column(name = "user_birth")
    @Setter
    private String birth;

    @Column(name = "del_yn")
    @Setter
    private String delYN;

    @Column(name = "user_work_time")
    @Setter
    private String workTime;

    @Column(name = "user_employ")
    @Setter
    private String employ;

    @Column(name = "lunar_yn")
    @Setter
    private String lunarYN;

    @OneToMany(mappedBy = "user")   // JPA에서는 mappedBy는 읽기 전용
    private List<Vacation> vacations;

    // vacation 추가 연관관계 편의 메소드
    public void addVacation(Vacation vacation) {
        vacation.setUser(this);
        vacations.add(vacation);
    }

    // 유저 생성자 (setter말고 해당 메소드 사용할 것)
    public static User addUser(String name, String birth, String employ, String workTime, String lunarYN) {
        User user = new User();
        user.name = name;
        user.birth = birth;
        user.employ = employ;
        user.workTime = workTime;
        user.lunarYN = lunarYN;
        user.delYN = "N";
        return user;
    }

    // 유저 삭제 (setter말고 해당 메소드 사용할 것)
    public void deleteUser() {
        this.delYN = "Y";
    }
}
