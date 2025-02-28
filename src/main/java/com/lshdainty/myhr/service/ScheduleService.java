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
    public Long addSchedule(Long userNo, String desc, ScheduleType type, LocalDateTime start, LocalDateTime end, Long addUserNo, String clientIP) {
        User user = userRepository.findUser(userNo);

        if (Objects.isNull(user)) { throw new IllegalArgumentException("user not found"); }

        // 여기에 잔여 휴가가 있는지 확인하는 로직 추가할 것
        // 스케쥴에 어떤 휴가를 선택했는지 추가하여 개수 차감필요
        // 자체 팝업 추가해서 선택한 날짜에 맞는
        List<Vacation> findVacations = vacationRepository.findVacationByParameterTime(userNo, LocalDateTime.now());
        List<Schedule> findSchedults = scheduleRepository.findSchedulesByUserNo(userNo);
        for (Schedule findSchedult : findSchedults) {
            log.info("test log1: {}", findSchedult.getStartDate());
            log.info("test log2: {}", findSchedult.getEndDate());
            log.info("test log3: {}", findSchedult.getType().calculate(findSchedult.getStartDate(), findSchedult.getEndDate()));
        }



        return 0L;

//        Schedule schedule = Schedule.addSchedule(user, desc, type, start, end, addUserNo, clientIP);
//        scheduleRepository.save(schedule);
//
//        return schedule.getId();
    }

    public List<Schedule> findSchedulesByUserNo(Long userNo) {
        return scheduleRepository.findSchedulesByUserNo(userNo);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, Long delUserNo, String clientIP) {
        Schedule findSchedule = scheduleRepository.findById(scheduleId);

        if (Objects.isNull(findSchedule)) { throw new IllegalArgumentException("schedule not found"); }

        if (findSchedule.getEndDate().isBefore(LocalDateTime.now())) { throw new IllegalArgumentException("Past schedules cannot be deleted"); }

        findSchedule.deleteSchedule(delUserNo, clientIP);
    }
}
