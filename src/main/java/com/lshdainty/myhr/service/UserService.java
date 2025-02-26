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
        User user = User.addUser(name, birth, employ, workTime, lunar);
        userRepository.save(user);
        return user.getId();
    }

    public User findUser(Long userId) {
        return userRepository.findUser(userId);
    }

    public List<User> findUsers() {
        return userRepository.findUsers();
    }

    @Transactional
    public void editUser(Long userId, String name, String birth, String employ, String workTime, String lunar) {
        User findUser = userRepository.findUser(userId);
        findUser.updateUser(name, birth, employ, workTime, lunar);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User findUser = userRepository.findUser(userId);
        findUser.deleteUser();
    }
}
