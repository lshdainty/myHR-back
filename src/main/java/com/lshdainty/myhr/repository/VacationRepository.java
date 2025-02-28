package com.lshdainty.myhr.repository;

import com.lshdainty.myhr.domain.Vacation;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VacationRepository {
    private final EntityManager em;

    // 휴가 저장
    public void save(Vacation vacation) {
        em.persist(vacation);
    }

    // 단건 휴가 조회(delete용)
    public Vacation findById(Long vacationId) {
        return em.find(Vacation.class, vacationId);
    }

    // 유저에 부여된 전체 휴가 조회
    public List<Vacation> findVacationsByUserNo(Long userNo) {
        return em.createQuery("select v from Vacation v where v.user.id = :userNo and v.delYN = :delYN", Vacation.class)
                .setParameter("userNo", userNo)
                .setParameter("delYN", "N")
                .getResultList();
    }

    // 연도에 해당하는 휴가 조회
    public List<Vacation> findVacationsByYear(String year) {
        return em.createQuery("select v from Vacation v where year(v.expiryDate) = :year and v.delYN = :delYN", Vacation.class)
                .setParameter("year", year)
                .setParameter("delYN", "N")
                .getResultList();
    }

    public List<Vacation> findVacationByParameterTime(Long userNo, LocalDateTime standardTime) {
        return em.createQuery("select v from Vacation v where v.user.id = :userNo and :standardTime between v.occurDate and v.expiryDate and delYN = :delYN", Vacation.class)
                .setParameter("userNo", userNo)
                .setParameter("standardTime", standardTime)
                .setParameter("delYN", "N")
                .getResultList();
    }
}
