package com.lshdainty.myhr.repository;

import com.lshdainty.myhr.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    // 신규 사용자 저장
    public void save(User user) {
        em.persist(user);
    }

    // userId로 단일 유저 검색
    public User findUser(int id) {
        return em.find(User.class, id);
    }

    // 전체 유저 목록 조회
    public List<User> findUsers() {
        return em.createQuery("select u from User u where u.delYN = :delYN", User.class)
                .setParameter("delYN", "N")
                .getResultList();
    }
}


