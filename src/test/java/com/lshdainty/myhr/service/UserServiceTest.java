package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("유저 서비스 테스트")
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원 가입")
    void signUp() {
        // given
        String name = "이서준";
        String birth = "19700723";
        String workTime = "9 ~ 6";
        String employ = "ADMIN";
        String lunar = "N";
        doNothing().when(userRepository).save(any(User.class));

        // when
        Long userId = userService.join(name, birth, employ, workTime, lunar);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("단건 유저 조회 테스트 (성공)")
    void findUserSuccessTest() {
        // given
        String name = "이서준";
        String birth = "19700723";
        String workTime = "9 ~ 6";
        String employ = "ADMIN";
        String lunar = "N";
        User user = User.createUser(name, birth, employ, workTime, lunar);
        given(userRepository.findById(1L)).willReturn(user);

        // when
        User findUser = userRepository.findById(1L);

        // then
        assertThat(findUser).isNotNull();
        assertThat(findUser.getName()).isEqualTo(name);
        assertThat(findUser.getBirth()).isEqualTo(birth);
        assertThat(findUser.getWorkTime()).isEqualTo(workTime);
        assertThat(findUser.getEmploy()).isEqualTo(employ);
        assertThat(findUser.getLunarYN()).isEqualTo(lunar);
        assertThat(findUser.getDelYN()).isEqualTo("N");
    }

    @Test
    @DisplayName("단건 유저 조회 테스트 (실패)")
    void findUserFailTest() {
        // given
        given(userRepository.findById(999L)).willReturn(null);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> userService.findUser(999L));
    }

    @Test
    @DisplayName("전체 유저 조회 테스트")
    void findUsersTest() {
        // given
        given(userRepository.findUsers())
                .willReturn(List.of(
                        User.createUser("이서준", "19700723", "ADMIN", "9 ~ 6", "N"),
                        User.createUser("김서연", "19701026", "BP", "8 ~ 5", "N"),
                        User.createUser("김지후", "19740115", "BP", "10 ~ 7", "Y")
                )
        );

        // when
        List<User> findUsers = userService.findUsers();

        // then
        assertThat(findUsers.size()).isEqualTo(3);
        assertThat(findUsers)
                .extracting(User::getName)
                .containsExactlyInAnyOrder("이서준", "김서연", "김지후");
    }

    @Test
    @DisplayName("유저 수정 테스트 (성공)")
    void editUserSuccessTest() {
        // given
        String name = "이서준";
        String birth = "19700723";
        String workTime = "9 ~ 6";
        String employ = "ADMIN";
        String lunar = "N";
        User user = User.createUser(name, birth, employ, workTime, lunar);

        given(userRepository.findById(1L)).willReturn(user);

        // when
        name = "이하은";
        workTime = "10 ~ 7";
        userService.editUser(1L, name, null, null, workTime, null);

        // then
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getBirth()).isEqualTo(birth);
        assertThat(user.getWorkTime()).isEqualTo(workTime);
        assertThat(user.getEmploy()).isEqualTo(employ);
        assertThat(user.getLunarYN()).isEqualTo(lunar);
    }

    @Test
    @DisplayName("유저 수정 테스트 (실패)")
    void editUserFailTest() {
        // given
        given(userRepository.findById(999L)).willReturn(null);

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> userService.editUser(999L, "이하은", null, null, null, null));
    }

    @Test
    @DisplayName("유저 삭제 테스트 (성공)")
    void deleteUserSuccessTest() {
        // given
        String name = "이서준";
        String birth = "19700723";
        String workTime = "9 ~ 6";
        String employ = "ADMIN";
        String lunar = "N";
        User user = User.createUser(name, birth, employ, workTime, lunar);

        given(userRepository.findById(1L)).willReturn(user);

        // when
        userService.deleteUser(1L);

        // then
        assertThat(user.getDelYN()).isEqualTo("Y");
    }

    @Test
    @DisplayName("유저 삭제 테스트 (실패)")
    void deleteUserFailTest() {
        // given
        given(userRepository.findById(999L)).willReturn(null);

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(999L));
    }
}
