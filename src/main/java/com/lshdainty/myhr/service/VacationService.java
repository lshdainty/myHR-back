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
    public Long addVacation(Long userNo, String name, String desc, VacationType type, float grantTime, LocalDateTime expiryDate, Long addUserNo, String clientIP) {
        User user = userRepository.findUser(userNo);

        if (Objects.isNull(user)) { throw new IllegalArgumentException("user not found"); }

        Vacation vacation = Vacation.addVacation(user, name, desc, type, grantTime, expiryDate, addUserNo, clientIP);
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
    public Vacation editVacation(Long vacationId, Long userNo, String reqName, String reqDesc, VacationType reqType, float reqGrantTime, LocalDateTime reqExpiryDate, Long addUserNo, String clientIP) {
        Vacation findVacation = vacationRepository.findById(vacationId);

        if (Objects.isNull(findVacation)) { throw new IllegalArgumentException("vacation not found"); }

        String name = "";
        if (Objects.isNull(reqName)) { name = findVacation.getName(); } else { name = reqName; }

        String desc = "";
        if (Objects.isNull(reqDesc)) { desc = findVacation.getDesc(); } else { desc = reqDesc; }

        VacationType type = null;
        if (Objects.isNull(reqType)) { type = findVacation.getType(); } else { type = reqType; }

        float grantTime = 0.0f;
        if (reqGrantTime == 0.0f) { grantTime = findVacation.getGrantTime(); } else { grantTime = reqGrantTime; }

        LocalDateTime expiryDate = null;
        if (Objects.isNull(reqExpiryDate)) { expiryDate = findVacation.getExpiryDate(); } else { expiryDate = reqExpiryDate; }

        User user = userRepository.findUser(userNo);

        if (Objects.isNull(user)) { throw new IllegalArgumentException("user not found"); }

        findVacation.deleteVacation(addUserNo, clientIP);

        Vacation newVacation = Vacation.addVacation(user, name, desc, type, grantTime, expiryDate, addUserNo, clientIP);
        vacationRepository.save(newVacation);

        Vacation findNewVacation = vacationRepository.findById(newVacation.getId());

        return findNewVacation;
    }

    @Transactional
    public void deleteVacation(Long vacationId, Long delUserNo, String clientIP) {
        Vacation findVacation = vacationRepository.findById(vacationId);

        if (Objects.isNull(findVacation)) { throw new IllegalArgumentException("vacation not found"); }

        findVacation.deleteVacation(delUserNo, clientIP);
    }
}
