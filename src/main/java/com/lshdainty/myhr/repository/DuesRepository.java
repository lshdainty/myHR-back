package com.lshdainty.myhr.repository;

import com.lshdainty.myhr.domain.Dues;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DuesRepository {
    private final EntityManager em;

    // 신규 회비 저장
    public void save(Dues dues) {
        em.persist(dues);
    }

    // 단건 회비 조회(delete용)
    public Dues findById(Long id) {
        return em.find(Dues.class, id);
    }

    // 전체 회비 조회
    public List<Dues> findDues() {
        return em.createQuery("select d from Dues d order by d.date", Dues.class)
                .getResultList();
    }

    // 년도에 해당하는 회비 조회
    public List<Dues> findDuesByYear(String year) {
        return em.createQuery("select d from Dues d where YEAR(d.date) = :year order by d.date", Dues.class)
                .setParameter("year", year)
                .getResultList();
    }

    // 회비 삭제
    public void delete(Dues dues) {
        em.remove(dues);
    }
}
