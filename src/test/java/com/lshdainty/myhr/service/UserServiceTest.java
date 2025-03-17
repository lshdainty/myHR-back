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

import static org.assertj.core.api.Assertions.assertThat;
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
    public void signUp() {
        // given
        String name = "이서준";
        String birth = "19700723";
        String workTime = "9 ~ 6";
        String employ = "ADMIN";
        String lunar = "N";
        doNothing().when(userRepository).save(any(User.class));

        // when
        Long userId = userService.join(name, birth, workTime, employ, lunar);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }
}
