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
    public Long join(User user) {
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
    public void editUser(Long userId, User user) {
        User findUser = userRepository.findUser(userId);

        if (!Objects.isNull(user.getName())) { findUser.setName(user.getName()); }
        if (!Objects.isNull(user.getBirth())) { findUser.setBirth(user.getBirth()); }
        if (!Objects.isNull(user.getEmploy())) { findUser.setEmploy(user.getEmploy()); }
        if (!Objects.isNull(user.getWorkTime())) { findUser.setWorkTime(user.getWorkTime()); }
        if (!Objects.isNull(user.getLunarYN())) { findUser.setLunarYN(user.getLunarYN()); }
    }

    @Transactional
    public void deleteUser(Long userId, String delYN) {
        User findUser = userRepository.findUser(userId);
        findUser.setDelYN(delYN);
    }
}
