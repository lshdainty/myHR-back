package com.lshdainty.myhr.repository;

import com.lshdainty.myhr.domain.Holiday;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HolidayRepository {
    private final EntityManager em;

    // 신규 휴일 저장
    public void save(Holiday holiday) {
        em.persist(holiday);
    }

    // 단건 휴일 조회
    public Holiday findHoliday(Long seq) {
        return em.find(Holiday.class, seq);
    }

    // 전체 휴일 조회
    public List<Holiday> findHolidays() {
        return em.createQuery("select h from Holiday h order by h.date", Holiday.class)
                .getResultList();
    }

    // 기간에 해당하는 휴일 조회
    public List<Holiday> findHolidaysByStartEndDate(String sDate, String eDate) {
        return em.createQuery("select h from Holiday h where h.date between :sDate and :eDate order by h.date", Holiday.class)
                .setParameter("sDate", sDate)
                .setParameter("eDate", eDate)
                .getResultList();
    }

    // 휴일 삭제
    public void delete(Holiday holiday) {
        em.remove(holiday);
    }
}
