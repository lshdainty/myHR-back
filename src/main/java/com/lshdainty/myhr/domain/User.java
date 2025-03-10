package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // -> protected Order() {}와 동일한 의미 (롬복으로 생성자 막기)
@Table(name = "deptop_user")
public class User {
    @Id @GeneratedValue
    @Column(name = "user_no")
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_pwd")
    private String pwd;

    @Column(name = "user_ip")
    private String ip;

    @Column(name = "user_role")
    private int role;

    @Column(name = "user_birth")
    private String birth;

    @Column(name = "del_yn")
    private String delYN;

    @Column(name = "user_work_time")
    private String workTime;

    @Column(name = "user_employ")
    private String employ;

    @Column(name = "lunar_yn")
    private String lunarYN;

    @OneToMany(mappedBy = "user")   // JPA에서는 mappedBy는 읽기 전용
    private List<Vacation> vacations;

    // 유저 생성자 (setter말고 해당 메소드 사용할 것)
    public static User addUser(String name, String birth, String employ, String workTime, String lunarYN) {
        User user = new User();
        user.name = name;
        user.birth = birth;
        user.employ = employ;
        user.workTime = workTime;
        user.lunarYN = lunarYN;
        user.delYN = "N";
        user.vacations = new ArrayList<>();
        return user;
    }

    // 유저 수정 (setter말고 해당 메소드 사용할 것)
    public void updateUser(String name, String birth, String employ, String workTime, String lunarYN) {
        if (!Objects.isNull(name)) { this.name = name; }
        if (!Objects.isNull(birth)) { this.birth = birth; }
        if (!Objects.isNull(employ)) { this.employ = employ; }
        if (!Objects.isNull(workTime)) { this.workTime = workTime; }
        if (!Objects.isNull(lunarYN)) { this.lunarYN = lunarYN; }
    }

    // 유저 삭제 (setter말고 해당 메소드 사용할 것)
    public void deleteUser() {
        this.delYN = "Y";
    }
}
