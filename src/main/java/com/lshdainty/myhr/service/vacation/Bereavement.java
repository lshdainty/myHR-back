package com.lshdainty.myhr.service.vacation;

import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.domain.Vacation;
import com.lshdainty.myhr.domain.VacationHistory;
import com.lshdainty.myhr.domain.VacationType;
import com.lshdainty.myhr.repository.HolidayRepositoryImpl;
import com.lshdainty.myhr.repository.UserRepositoryImpl;
import com.lshdainty.myhr.repository.VacationHistoryRepositoryImpl;
import com.lshdainty.myhr.repository.VacationRepositoryImpl;
import com.lshdainty.myhr.service.UserService;
import com.lshdainty.myhr.service.VacationService;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bereavement extends VacationService {
    MessageSource ms;
    VacationRepositoryImpl vacationRepositoryImpl;
    VacationHistoryRepositoryImpl vacationHistoryRepositoryImpl;
    UserRepositoryImpl userRepositoryImpl;
    HolidayRepositoryImpl holidayRepositoryImpl;
    UserService userService;

    public Bereavement(
            MessageSource ms,
            VacationRepositoryImpl vacationRepositoryImpl,
            VacationHistoryRepositoryImpl vacationHistoryRepositoryImpl,
            UserRepositoryImpl userRepositoryImpl,
            HolidayRepositoryImpl holidayRepositoryImpl,
            UserService userService
    ) {
        super(ms, vacationRepositoryImpl, vacationHistoryRepositoryImpl, userRepositoryImpl, holidayRepositoryImpl, userService);
        this.ms = ms;
        this.vacationRepositoryImpl = vacationRepositoryImpl;
        this.vacationHistoryRepositoryImpl = vacationHistoryRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
        this.holidayRepositoryImpl = holidayRepositoryImpl;
        this.userService = userService;
    }

    @Override
    public Long registVacation(String userId, String desc, VacationType type, BigDecimal grantTime, LocalDateTime occurDate, LocalDateTime expiryDate, String crtUserId, String clientIP) {
        User user = userService.checkUserExist(userId);

        Vacation vacation = Vacation.createVacation(user, type, grantTime, occurDate, expiryDate, crtUserId, clientIP);
        vacationRepositoryImpl.save(vacation);

        VacationHistory history = VacationHistory.createRegistVacationHistory(vacation, desc, grantTime, crtUserId, clientIP);
        vacationHistoryRepositoryImpl.save(history);

        return vacation.getId();
    }
}
