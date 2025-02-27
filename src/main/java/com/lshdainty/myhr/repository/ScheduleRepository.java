package com.lshdainty.myhr.repository;

import com.lshdainty.myhr.domain.Schedule;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepository {
    private final EntityManager em;

    // 신규 스케쥴 저장
    public void save(Schedule schedule) {
        em.persist(schedule);
    }

    // 스케줄 단건 조회(delete용)
    public Schedule findById(Long scheduleId) {
        return em.find(Schedule.class, scheduleId);
    }

    // 유저 스케줄 조회
    public List<Schedule> findSchedulesByUserNo(Long userNo) {
        return em.createQuery("select s from Schedule s where s.user.id = :userNo and s.delYN = :delYN", Schedule.class)
                .setParameter("userNo", userNo)
                .setParameter("delYN", "N")
                .getResultList();
    }
}
