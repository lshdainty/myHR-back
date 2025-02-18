package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "deptop_recommend")
public class Recommend {
    @Id @GeneratedValue
    @Column(name = "recommend_seq")
    private Long seq;

    @Column(name = "recommend_name")
    @Setter
    private String name;

    @Column(name = "recommend_date")
    @NotNull
    @Setter
    private String date;
}
