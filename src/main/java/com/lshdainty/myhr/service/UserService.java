package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Long join(String name, String birth, String employ, String workTime, String lunar) {
        User user = User.createUser(name, birth, employ, workTime, lunar);
        userRepository.save(user);

        // 유저 id 없으면 에러 반환
        if (Objects.isNull(user.getId())) { throw new IllegalArgumentException("failed to save user"); }

        return user.getId();
    }

    public User findUser(Long userId) {
        User user = userRepository.findById(userId);

        // 유저 없으면 에러 반환
        if (Objects.isNull(user) || user.getDelYN().equals("Y")) { throw new IllegalArgumentException("user not found"); }

        return user;
    }

    public List<User> findUsers() {
        return userRepository.findUsers();
    }

    @Transactional
    public void editUser(Long userId, String name, String birth, String employ, String workTime, String lunar) {
        User findUser = userRepository.findById(userId);

        // 유저 없으면 에러 반환
        if (Objects.isNull(findUser) || findUser.getDelYN().equals("Y")) { throw new IllegalArgumentException("user not found"); }

        findUser.updateUser(name, birth, employ, workTime, lunar);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User findUser = userRepository.findById(userId);

        // 유저 없으면 에러 반환
        if (Objects.isNull(findUser) || findUser.getDelYN().equals("Y")) { throw new IllegalArgumentException("user not found"); }

        findUser.deleteUser();
    }
}
