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
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VacationService {
    private final VacationRepository vacationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long addVacation(Long userNo, String name, String desc, VacationType type, float grantTime, LocalDateTime expiryDate, Long addUserNo, String ip) {
        User user = userRepository.findUser(userNo);

        if (Objects.isNull(user)) { throw new IllegalArgumentException("user not found"); }

        Vacation vacation = Vacation.addVacation(user, name, desc, type, grantTime, expiryDate, addUserNo, ip);
        vacationRepository.save(vacation);

        return vacation.getId();
    }

    public List<Vacation> findVacationsByUser(Long userNo) {
        return vacationRepository.findVacationsByUserNo(userNo);
    }

    public List<User> findVacationsByUserGroup() {
        return userRepository.findUsersWithVacations();
    }

    @Transactional
    public void deleteVacation(Long vacationId, Long delUserNo, String clientIP) {
        Vacation findVacation = vacationRepository.findById(vacationId);
        findVacation.deleteVacation(delUserNo, clientIP);
    }
}
