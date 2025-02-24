package com.lshdainty.myhr.repository;

import com.lshdainty.myhr.domain.Vacation;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VacationRepository {
    private final EntityManager em;

    // 휴가 저장
    public void save(Vacation vacation) {
        em.persist(vacation);
    }

    // 유저에 부여된 전체 휴가 조회
    public List<Vacation> findVacationsByUserNo(Long userNo) {
        return em.createQuery("select v from Vacation v where v.user.id = :userNo", Vacation.class)
                .setParameter("userNo", userNo)
                .getResultList();
    }

    // 연도에 해당하는 휴가 조회
    public List<Vacation> findVacationsByYear(String year) {
        return em.createQuery("select v from Vacation v where year(v.expirationDate) = :year")
                .setParameter("year", year)
                .getResultList();
    }
}
