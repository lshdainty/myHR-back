package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.*;
import com.lshdainty.myhr.repository.*;
import com.lshdainty.myhr.util.MyhrTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ScheduleService {
    private final MessageSource ms;
    private final ScheduleRepositoryImpl scheduleRepositoryImpl;
    private final UserService userService;

    @Transactional
    public Long registSchedule(String userId, ScheduleType type, String desc, LocalDateTime start, LocalDateTime end, String crtUserId, String clientIP) {
        // 유저 조회
        User user = userService.checkUserExist(userId);

        if (MyhrTime.isAfterThanEndDate(start, end)) { throw new IllegalArgumentException(ms.getMessage("error.validate.startIsAfterThanEnd", null, null)); }

        Schedule schedule = Schedule.createSchedule(user, desc, type, start, end, crtUserId, clientIP);

        // 휴가 등록
        scheduleRepositoryImpl.save(schedule);

        return schedule.getId();
    }

    public List<Schedule> findSchedulesByUserId(String userId) {
        return scheduleRepositoryImpl.findSchedulesByUserId(userId);
    }

    public List<Schedule> findSchedulesByPeriod(LocalDateTime start, LocalDateTime end) {
        if (MyhrTime.isAfterThanEndDate(start, end)) { throw new IllegalArgumentException(ms.getMessage("error.validate.startIsAfterThanEnd", null, null)); }
        return scheduleRepositoryImpl.findSchedulesByPeriod(start, end);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, String delUserId, String clientIP) {
        Schedule schedule = checkScheduleExist(scheduleId);

        if (schedule.getEndDate().isBefore(LocalDateTime.now())) { throw new IllegalArgumentException(ms.getMessage("error.validate.delete.isBeforeThanNow", null, null)); }

        schedule.deleteSchedule(delUserId, clientIP);
    }

    public Schedule checkScheduleExist(Long scheduleId) {
        Optional<Schedule> schedule = scheduleRepositoryImpl.findById(scheduleId);
        if (schedule.isEmpty() || schedule.get().getDelYN().equals("Y")) { throw new IllegalArgumentException(ms.getMessage("error.notfound.schedule", null, null)); }
        return schedule.get();
    }
}
