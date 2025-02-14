package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
@Table(name = "deptop_holiday")
public class Holiday {
    @Id @GeneratedValue
    @Column(name = "holiday_seq")
    private Long seq;

    @Column(name = "holiday_name")
    private String name;

    @Column(name = "holiday_date")
    @NotNull
    private String date;
}
