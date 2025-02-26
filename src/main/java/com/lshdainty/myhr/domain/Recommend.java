package com.lshdainty.myhr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // -> protected Order() {}와 동일한 의미 (롬복으로 생성자 막기)
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

    // 휴일 생성자 (setter말고 해당 메소드 사용할 것)
    public static Recommend addRecommend(String name, String date) {
        Recommend recommend = new Recommend();
        recommend.name = name;
        recommend.date = date;
        return recommend;
    }
}
