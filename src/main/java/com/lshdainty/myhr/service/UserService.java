package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final MessageSource ms;
    private final UserRepositoryImpl userRepositoryImpl;

    @Transactional
    public Long join(String name, String birth, String employ, String workTime, String lunar) {
        User user = User.createUser(name, birth, employ, workTime, lunar);
        userRepositoryImpl.save(user);
        return user.getId();
    }

    public User findUser(Long userNo) {
        return checkUserExist(userNo);
    }

    public List<User> findUsers() {
        return userRepositoryImpl.findUsers();
    }

    @Transactional
    public void editUser(Long userId, String name, String birth, String employ, String workTime, String lunar) {
        User user = checkUserExist(userId);
        user.updateUser(name, birth, employ, workTime, lunar);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = checkUserExist(userId);
        user.deleteUser();
    }

    public User checkUserExist(Long userNo) {
        Optional<User> findUser = userRepositoryImpl.findById(userNo);
        if ((findUser.isEmpty()) || findUser.get().getDelYN().equals("Y")) { throw new IllegalArgumentException(ms.getMessage("error.notfound.user", null, null)); }
        return findUser.get();
    }
}