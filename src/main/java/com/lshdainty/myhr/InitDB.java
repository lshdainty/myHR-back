package com.lshdainty.myhr;

import com.lshdainty.myhr.domain.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initSetMember();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void initSetMember() {
            saveMember("이서준", "19700723", "9 ~ 6", "ADMIN", "N", "N");
            saveMember("김서연", "19701026", "8 ~ 5", "BP", "N", "N");
            saveMember("김지후", "19740115", "10 ~ 7", "BP", "Y", "N");
            saveMember("이준우", "19800430", "9 ~ 6", "BP", "N", "Y");
            saveMember("조민서", "19921220", "9 ~ 6", "ADMIN", "N", "N");
            saveMember("이하은", "18850902", "8 ~ 5", "ADMIN", "N", "N");
        }

        public void initHoliday() {

        }

        public void saveMember(String name, String birth, String workTime, String employ, String lunar, String del) {
            User user = new User();
            user.setName(name);
            user.setBirth(birth);
            user.setWorkTime(workTime);
            user.setEmploy(employ);
            user.setLunarYN(lunar);
            user.setDelYN(del);
            em.persist(user);
        }
    }
}
