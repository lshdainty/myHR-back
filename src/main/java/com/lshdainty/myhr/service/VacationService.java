package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.domain.Vacation;
import com.lshdainty.myhr.domain.VacationType;
import com.lshdainty.myhr.repository.UserRepository;
import com.lshdainty.myhr.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VacationService {
    private final VacationRepository vacationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(Long userNo, String name, String desc, VacationType type, float granted, LocalDateTime expiration, Long createUserNo, String ip) {
        User user = userRepository.findUser(userNo);
        Vacation vacation = Vacation.addVacation(user, name, desc, type, granted, expiration, createUserNo, ip);
        vacationRepository.save(vacation);

        return vacation.getId();
    }

    public List<User> findVacationsByUserGroup() {
        return userRepository.findUsersWithVacations();
    }

    @Transactional
    public void deleteVacation(Long vacationId, Long deleteUserNo, String clientIP) {
        Vacation findVacation = vacationRepository.findById(vacationId);
        findVacation.deleteVacation(deleteUserNo, clientIP);
    }
}
