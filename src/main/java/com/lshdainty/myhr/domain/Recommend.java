package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
@Table(name = "deptop_recommend")
public class Recommend {
    @Id @GeneratedValue
    @Column(name = "recommend_seq")
    private Long seq;

    @Column(name = "recommend_name")
    private String name;

    @Column(name = "recommend_date")
    @NotNull
    private String date;
}
