package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "deptop_dues")
public class Dues {
    @Id @GeneratedValue
    @Column(name = "dues_seq")
    private Long seq;

    @Column(name = "dues_user_name")
    private String userName;

    @Column(name = "dues_amount")
    private int amount;

    @Column(name = "dues_type")
    private String type;

    @Column(name = "dues_date")
    private String date;

    @Column(name = "dues_detail")
    private String detail;
}
