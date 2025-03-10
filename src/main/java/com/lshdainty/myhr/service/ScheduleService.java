package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.*;
import com.lshdainty.myhr.repository.HolidayRepository;
import com.lshdainty.myhr.repository.ScheduleRepository;
import com.lshdainty.myhr.repository.UserRepository;
import com.lshdainty.myhr.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final VacationRepository vacationRepository;
    private final HolidayRepository holidayRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long addSchedule(Long userNo, Long vacationId, ScheduleType type, String desc, LocalDateTime start, LocalDateTime end, Long addUserNo, String clientIP) {
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

        // 이제까지 해당 휴가에 사용된 스케줄 리스트 가져오기
        List<Schedule> findSchedules = scheduleRepository.findCountByVacation(vacation);

        // 사용된 시간 계산
        BigDecimal used = new BigDecimal(0);
        for (Schedule schedule : findSchedules) {
            used = used.add(calculateRealUse(schedule));
        }

        // 휴가 등록이 가능한지 확인을 위한 스케줄 생성
        Schedule schedule = Schedule.createSchedule(user, vacation, desc, type, start, end, addUserNo, clientIP);

        // 사용할 휴가의 실제 사용 시간 계산
        BigDecimal toBeUse = calculateRealUse(schedule);

        // 남은 시간 계산
        // grantTime - totalUsed - tobeuse < 0
        if (vacation.getGrantTime().subtract(used).subtract(toBeUse).compareTo(BigDecimal.ZERO) < 0) { throw new IllegalArgumentException("there is not enough vacation left"); }

        // 휴가 등록
        scheduleRepository.save(schedule);

        return schedule.getId();
    }

    @Transactional
    public Long addSchedule(Long userNo, ScheduleType type, String desc, LocalDateTime start, LocalDateTime end, Long addUserNo, String clientIP) {
        // 유저 조회
        User user = userRepository.findById(userNo);

        // 유저 없으면 에러 반환
        if (Objects.isNull(user) || user.getDelYN().equals("Y")) { throw new IllegalArgumentException("user not found"); }

        Schedule schedule = Schedule.createSchedule(user, null, desc, type, start, end, addUserNo, clientIP);

        // 휴가 등록
        scheduleRepository.save(schedule);

        return schedule.getId();
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

    /**
     * 두 배열간 중복을 제거하고 차를 구하여 반환하는 함수
     *
     * @param source startDate ~ endDate 사이에 있는 모든 날짜들
     * @param target 주말 리스트, 휴일 리스트 등등
     * @return source - target을 한 날짜 리스트 반환
     */
    public List<LocalDate> subtractDates(List<LocalDate> source, List<LocalDate> target) {
        Set<LocalDate> sourceSet = new HashSet<>(source);
        Set<LocalDate> targetSet = new HashSet<>(target);
        sourceSet.removeAll(targetSet);
        return sourceSet.stream().map(LocalDate::from).collect(Collectors.toList());
    }

    /**
     * 두 배열간 중복을 제거하고 차를 구하여 반환하는 함수
     *
     * @param schedule 계산 대상 스케줄
     * @return 스케줄의 실제 사용 시간 (휴가, 주말 등 제외)
     */
    public BigDecimal calculateRealUse(Schedule schedule) {
        BigDecimal use = new BigDecimal(0);

        List<LocalDate> sources = schedule.getBetweenDates();
        log.debug("calculateRealUse sources : {}", sources);

        List<Holiday> holidays = holidayRepository.findHolidaysByStartEndDate(
                schedule.getStartDate().format(DateTimeFormatter.BASIC_ISO_DATE),
                schedule.getEndDate().format(DateTimeFormatter.BASIC_ISO_DATE)
        );

        List<LocalDate> holidayDates = holidays.stream()
                .map(h -> LocalDate.parse(h.getDate(), DateTimeFormatter.BASIC_ISO_DATE))
                .toList();

        List<LocalDate> targets = schedule.getBetweenDatesByDayOfWeek(new int[]{6, 7});
        targets.addAll(holidayDates);
        log.debug("calculateRealUse targets : {}", targets);

        List<LocalDate> results = subtractDates(sources, targets);

        use = use.add(schedule.getType().convertToValue(results.size()));

        return use;
    }
}
