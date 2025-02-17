package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public int join(User user) {
        userRepository.save(user);
        return user.getId();
    }

    public User findUserByUserId(int userId) {
        return userRepository.findUserByUserId(userId);
    }

    public List<User> findUsers() {
        return userRepository.findUsers();
    }
}
