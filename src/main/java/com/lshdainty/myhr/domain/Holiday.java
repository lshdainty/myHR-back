package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "deptop_holiday")
public class Holiday {
    @Id @GeneratedValue
    @Column(name = "holiday_seq")
    private Long seq;

    @Column(name = "holiday_name")
    @Setter
    private String name;

    @Column(name = "holiday_date")
    @NotNull
    @Setter
    private String date;
}
