package com.lshdainty.myhr.repository;

import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.domain.Vacation;
import com.lshdainty.myhr.domain.VacationType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(UserRepository.class)
@Transactional
@DisplayName("JPA 유저 레포지토리 테스트")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;


    @Test
    @DisplayName("유저 등록")
    void save() {
        // given
        String name = "홍길동";
        String birth = "19700204";
        String employ = "BP";
        String workTime = "9 ~ 6";
        String lunarYN = "N";

        User user = User.addUser(name, birth, employ, workTime, lunarYN);

        // when
        userRepository.save(user);

        User findUser = userRepository.findById(user.getId());

        // then
        assertThat(findUser).isEqualTo(user);
        assertThat(findUser.getName()).isEqualTo(name);
        assertThat(findUser.getBirth()).isEqualTo(birth);
        assertThat(findUser.getEmploy()).isEqualTo(employ);
        assertThat(findUser.getWorkTime()).isEqualTo(workTime);
        assertThat(findUser.getLunarYN()).isEqualTo(lunarYN);
    }

    @Test
    @DisplayName("단일 유저 조회")
    void getUser() {
        // given
        String name = "홍길동";
        String birth = "19700204";
        String employ = "BP";
        String workTime = "9 ~ 6";
        String lunarYN = "N";

        User user = User.addUser(name, birth, employ, workTime, lunarYN);
        userRepository.save(user);

        // when
        User findUser = userRepository.findById(user.getId());

        // then
        assertThat(findUser).isEqualTo(user);
        assertThat(findUser.getName()).isEqualTo(name);
        assertThat(findUser.getBirth()).isEqualTo(birth);
        assertThat(findUser.getEmploy()).isEqualTo(employ);
        assertThat(findUser.getWorkTime()).isEqualTo(workTime);
        assertThat(findUser.getLunarYN()).isEqualTo(lunarYN);
    }

    @Test
    @DisplayName("유저 리스트 조회")
    void getUsers() {
        // given
        String[] names = {"이서준", "김서연", "김지후"};
        String[] births = {"19700723", "19701026", "19740115"};
        String[] employs = {"9 ~ 6", "8 ~ 5", "10 ~ 7"};
        String[] workTimes = {"ADMIN", "BP", "BP"};
        String[] lunarYNs = {"N", "N", "Y"};

        for (int i = 0; i < names.length; i++) {
            User user = User.addUser(names[i], births[i], employs[i], workTimes[i], lunarYNs[i]);
            userRepository.save(user);
        }

        // when
        List<User> users = userRepository.findUsers();

        // then
        assertThat(users.size()).isEqualTo(names.length);
    }

    @Test
    @DisplayName("유저 삭제")
    void deleteUser() {
        // given
        String name = "홍길동";
        String birth = "19700204";
        String employ = "BP";
        String workTime = "9 ~ 6";
        String lunarYN = "N";

        User user = User.addUser(name, birth, employ, workTime, lunarYN);
        userRepository.save(user);

        // when
        user.deleteUser();
        User findUser = userRepository.findById(user.getId());

        // then
        assertThat(findUser.getDelYN()).isEqualTo("Y");
    }

    @Test
    @DisplayName("유저 수정")
    void updateUser() {
        // given
        String name = "홍길동";
        String birth = "19700204";
        String employ = "BP";
        String workTime = "9 ~ 6";
        String lunarYN = "N";

        User user = User.addUser(name, birth, employ, workTime, lunarYN);
        userRepository.save(user);

        name = "이서준";
        workTime = "10 ~ 7";

        // when
        user.updateUser(name, birth, employ, workTime, lunarYN);
        User findUser = userRepository.findById(user.getId());

        // then
        assertThat(findUser.getName()).isEqualTo(name);
        assertThat(findUser.getBirth()).isEqualTo(birth);
        assertThat(findUser.getEmploy()).isEqualTo(employ);
        assertThat(findUser.getWorkTime()).isEqualTo(workTime);
        assertThat(findUser.getLunarYN()).isEqualTo(lunarYN);
    }

    @Test
    @DisplayName("유저가 가지고 있는 휴가 리스트 조회")
    void getUserWithVacations() {
        // given
        User userA = User.addUser("이서준", "19700723", "9 ~ 6", "ADMIN", "N");
        User userB = User.addUser("김서연", "19701026", "8 ~ 5", "BP", "N");
        User userC = User.addUser("김지후", "19740115", "10 ~ 7", "BP", "Y");
        userRepository.save(userA);
        userRepository.save(userB);
        userRepository.save(userC);

        LocalDateTime now = LocalDateTime.now();
        em.persist(Vacation.addVacation(userA, "1분기 휴가", "", VacationType.BASIC, new BigDecimal("32"), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), 0L, "127.0.0.1"));
        em.persist(Vacation.addVacation(userB, "1분기 휴가", "", VacationType.BASIC, new BigDecimal("32"), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), 0L, "127.0.0.1"));
        em.persist(Vacation.addVacation(userC, "1분기 휴가", "작년 하루 사용", VacationType.BASIC, new BigDecimal("24"), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), 0L, "127.0.0.1"));
        em.persist(Vacation.addVacation(userA, "2분기 휴가", "", VacationType.BASIC, new BigDecimal("32"), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), LocalDateTime.of(now.getYear(), 4, 1, 0, 0, 0), 0L, "127.0.0.1"));
        em.persist(Vacation.addVacation(userB, "2분기 휴가", "", VacationType.BASIC, new BigDecimal("32"), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), LocalDateTime.of(now.getYear(), 4, 1, 0, 0, 0), 0L, "127.0.0.1"));

        userB.deleteUser();

        // when
        List<User> users = userRepository.findUsersWithVacations();
        int countA = 0;
        int countC = 0;
        for (User user : users) {
            List<Vacation> lists = user.getVacations();

            for (Vacation vacation : lists) {
                if (user.getName().equals(userA.getName())) {
                    countA++;
                } else {
                    countC++;
                }
            }
        }

        // then
        assertThat(users.size()).isEqualTo(2);
        assertThat(countA).isEqualTo(2);
        assertThat(countC).isEqualTo(1);
    }
}