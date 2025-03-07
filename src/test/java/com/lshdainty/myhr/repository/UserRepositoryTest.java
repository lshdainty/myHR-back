package com.lshdainty.myhr.repository;

import com.lshdainty.myhr.domain.Holiday;
import com.lshdainty.myhr.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

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
}