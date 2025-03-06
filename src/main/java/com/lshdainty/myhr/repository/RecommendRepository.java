package com.lshdainty.myhr.repository;

import com.lshdainty.myhr.domain.Recommend;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendRepository {
    private final EntityManager em;

    // 전체 권장휴일 조회
    public List<Recommend> findRecommends() {
        return em.createQuery("select r from Recommend r order by r.date", Recommend.class)
                .getResultList();
    }

    // 기간에 해당하는 권장휴일 조회
    public List<Recommend> findRecommendsByStartEndDate(String start, String end) {
        return em.createQuery("select r from Recommend r where r.date between :start and :end order by r.date", Recommend.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}
