package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "deptop_user")
public class User {
    @Id @GeneratedValue
    @Column(name = "user_no")
    private int id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_pwd")
    private String pwd;

    @Column(name = "user_ip")
    private String ip;

    @Column(name = "user_role")
    private int role;

    @Column(name = "del_yn")
    private String delYN;

    @Column(name = "user_work_time")
    private String workTime;

    @Column(name = "user_employ")
    private String employ;

    @Column(name = "lunar_yn")
    private String lunarYN;
}
