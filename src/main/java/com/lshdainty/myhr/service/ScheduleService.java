package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.Schedule;
import com.lshdainty.myhr.domain.ScheduleType;
import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.domain.Vacation;
import com.lshdainty.myhr.repository.ScheduleRepository;
import com.lshdainty.myhr.repository.UserRepository;
import com.lshdainty.myhr.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final VacationRepository vacationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long addSchedule(Long userNo, String desc, Long vacationId, ScheduleType type, LocalDateTime start, LocalDateTime end, Long addUserNo, String clientIP) {
        // 유저 조회
        User user = userRepository.findById(userNo);

        // 유저 없으면 에러 반환
        if (Objects.isNull(user) || user.getDelYN().equals("Y")) { throw new IllegalArgumentException("user not found"); }

        // 휴가 조회
        Vacation vacation = vacationRepository.findById(vacationId);

        // 사용하려는 휴가가 없으면 에러 반환
        if (Objects.isNull(vacation) || vacation.getDelYN().equals("Y")) { throw new IllegalArgumentException("vacation not found"); }
        // 사용기한이 지난 휴가면 사용불가
        if (vacation.getExpiryDate().isBefore(LocalDateTime.now())) { throw new IllegalArgumentException("this vacation has expired"); }

        // 해당 휴가에 사용된 스케줄 리스트 가져오기
        List<Schedule> findSchedults = scheduleRepository.findCountByVacation(vacation);

        // 사용된 시간 계산
        BigDecimal totalUsed = Schedule.calculateUsed(findSchedults);

        // 남은 시간 계산
        // grantTime - totalUsed - type.calculate(start, end) < 0
        if (vacation.getGrantTime().subtract(totalUsed).subtract(type.calculate(start, end)).compareTo(BigDecimal.ZERO) < 0) { throw new IllegalArgumentException("there is not enough vacation left"); }

        // 휴가 등록
        Schedule schedule = Schedule.addSchedule(user, vacation, desc, type, start, end, addUserNo, clientIP);
        scheduleRepository.save(schedule);

        return schedule.getId();
    }

    @Transactional
    public Long addSchedule(Long userNo, String desc, ScheduleType type, LocalDateTime start, LocalDateTime end, Long addUserNo, String clientIP) {
        return 0L;
    }

    public List<Schedule> findSchedulesByUserNo(Long userNo) {
        return scheduleRepository.findSchedulesByUserNo(userNo);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, Long delUserNo, String clientIP) {
        Schedule schedule = scheduleRepository.findById(scheduleId);

        if (Objects.isNull(schedule) || schedule.getDelYN().equals("Y")) { throw new IllegalArgumentException("schedule not found"); }

        if (schedule.getEndDate().isBefore(LocalDateTime.now())) { throw new IllegalArgumentException("Past schedules cannot be deleted"); }

        schedule.deleteSchedule(delUserNo, clientIP);
    }
}
