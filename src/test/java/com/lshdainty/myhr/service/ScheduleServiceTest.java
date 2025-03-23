package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.*;
import com.lshdainty.myhr.repository.HolidayRepository;
import com.lshdainty.myhr.repository.ScheduleRepository;
import com.lshdainty.myhr.repository.UserRepository;
import com.lshdainty.myhr.repository.VacationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private VacationRepository vacationRepository;
    @Mock
    private HolidayRepository holidayRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    @DisplayName("휴가 스케줄 추가 성공 테스트")
    void addScheduleWithVacationSuccessTest() {
        // Given
        Long userNo = 1L;
        Long vacationId = 1L;
        ScheduleType type = ScheduleType.DAYOFF;
        String desc = "연차 휴가";
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Long addUserNo = 1L;
        String clientIP = "127.0.0.1";

        User mockUser = new User();
        Vacation mockVacation = Vacation.createVacation(mockUser, "연차", "연차 휴가", VacationType.BASIC, new BigDecimal("8.0000"), start.minusDays(30), end.plusDays(30), addUserNo, clientIP);
        Schedule mockSchedule = Schedule.createSchedule(mockUser, mockVacation, desc, type, start, end, addUserNo, clientIP);

        given(userRepository.findById(userNo)).willReturn(mockUser);
        given(vacationRepository.findById(vacationId)).willReturn(mockVacation);
        given(scheduleRepository.findCountByVacation(any(Vacation.class))).willReturn(Collections.emptyList());
        given(holidayRepository.findHolidaysByStartEndDate(any(), any())).willReturn(Collections.emptyList());
        given(scheduleRepository.save(any(Schedule.class))).willReturn(mockSchedule);

        // When
        Long scheduleId = scheduleService.addSchedule(userNo, vacationId, type, desc, start, end, addUserNo, clientIP);

        // Then
        assertThat(scheduleId).isNotNull();
        then(scheduleRepository).should().save(any(Schedule.class));
    }

    @Test
    @DisplayName("휴가 스케줄 추가 실패 테스트 - 사용자 없음")
    void addScheduleWithVacationFailUserNotFoundTest() {
        // Given
        Long userNo = 999L;
        given(userRepository.findById(userNo)).willReturn(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                scheduleService.addSchedule(userNo, 1L, ScheduleType.DAYOFF, "desc", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1L, "127.0.0.1"));
    }

    @Test
    @DisplayName("휴가 스케줄 추가 실패 테스트 - 휴가 없음")
    void addScheduleWithVacationFailVacationNotFoundTest() {
        // Given
        Long userNo = 1L;
        Long vacationId = 999L;
        User mockUser = new User();
        given(userRepository.findById(userNo)).willReturn(mockUser);
        given(vacationRepository.findById(vacationId)).willReturn(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                scheduleService.addSchedule(userNo, vacationId, ScheduleType.DAYOFF, "desc", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1L, "127.0.0.1"));
    }

    @Test
    @DisplayName("휴가 스케줄 추가 실패 테스트 - 만료된 휴가")
    void addScheduleWithVacationFailExpiredVacationTest() {
        // Given
        Long userNo = 1L;
        Long vacationId = 1L;
        User mockUser = new User();
        Vacation mockVacation = Vacation.createVacation(mockUser, "연차", "연차 휴가", VacationType.BASIC, new BigDecimal("8.0000"), LocalDateTime.now().minusDays(60), LocalDateTime.now().minusDays(30), 1L, "127.0.0.1");

        given(userRepository.findById(userNo)).willReturn(mockUser);
        given(vacationRepository.findById(vacationId)).willReturn(mockVacation);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                scheduleService.addSchedule(userNo, vacationId, ScheduleType.DAYOFF, "desc", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1L, "127.0.0.1"));
    }

    @Test
    @DisplayName("휴가 스케줄 추가 실패 테스트 - 휴가 부족")
    void addScheduleWithVacationFailNotEnoughVacationTest() {
        // Given
        Long userNo = 1L;
        Long vacationId = 1L;
        User mockUser = new User();
        Vacation mockVacation = Vacation.createVacation(mockUser, "연차", "연차 휴가", VacationType.BASIC, new BigDecimal("8.0000"), LocalDateTime.now().minusDays(30), LocalDateTime.now().plusDays(30), 1L, "127.0.0.1");

        given(userRepository.findById(userNo)).willReturn(mockUser);
        given(vacationRepository.findById(vacationId)).willReturn(mockVacation);
        given(scheduleRepository.findCountByVacation(any(Vacation.class))).willReturn(Collections.emptyList());
        given(holidayRepository.findHolidaysByStartEndDate(any(), any())).willReturn(Collections.emptyList());

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                scheduleService.addSchedule(userNo, vacationId, ScheduleType.DAYOFF, "desc", LocalDateTime.now(), LocalDateTime.now().plusDays(2), 1L, "127.0.0.1"));
    }

    @Test
    @DisplayName("비휴가 스케줄 추가 성공 테스트")
    void addScheduleWithoutVacationSuccessTest() {
        // Given
        Long userNo = 1L;
        ScheduleType type = ScheduleType.EDUCATION;
        String desc = "교육";
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Long addUserNo = 1L;
        String clientIP = "127.0.0.1";

        User mockUser = new User();
        Schedule mockSchedule = Schedule.createSchedule(mockUser, null, desc, type, start, end, addUserNo, clientIP);

        given(userRepository.findById(userNo)).willReturn(mockUser);
        given(scheduleRepository.save(any(Schedule.class))).willReturn(mockSchedule);

        // When
        Long scheduleId = scheduleService.addSchedule(userNo, type, desc, start, end, addUserNo, clientIP);

        // Then
        assertThat(scheduleId).isNotNull();
        then(scheduleRepository).should().save(any(Schedule.class));
    }

    @Test
    @DisplayName("사용자별 스케줄 조회 테스트")
    void findSchedulesByUserNoTest() {
        // Given
        Long userNo = 1L;
        List<Schedule> mockSchedules = Arrays.asList(
                Schedule.createSchedule(new User(), null, "스케줄1", ScheduleType.DAYOFF, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1L, "127.0.0.1"),
                Schedule.createSchedule(new User(), null, "스케줄2", ScheduleType.EDUCATION, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3), 1L, "127.0.0.1")
        );

        given(scheduleRepository.findSchedulesByUserNo(userNo)).willReturn(mockSchedules);

        // When
        List<Schedule> schedules = scheduleService.findSchedulesByUserNo(userNo);

        // Then
        assertThat(schedules).hasSize(2);
        assertThat(schedules).extracting("desc").containsExactly("스케줄1", "스케줄2");
    }

    @Test
    @DisplayName("스케줄 삭제 성공 테스트")
    void deleteScheduleSuccessTest() {
        // Given
        Long scheduleId = 1L;
        Long delUserNo = 1L;
        String clientIP = "127.0.0.1";
        Schedule mockSchedule = Schedule.createSchedule(new User(), null, "스케줄", ScheduleType.DAYOFF, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 1L, "127.0.0.1");

        given(scheduleRepository.findById(scheduleId)).willReturn(mockSchedule);

        // When
        scheduleService.deleteSchedule(scheduleId, delUserNo, clientIP);

        // Then
        assertThat(mockSchedule.getDelYN()).isEqualTo("Y");
    }

    @Test
    @DisplayName("스케줄 삭제 실패 테스트 - 스케줄 없음")
    void deleteScheduleFailScheduleNotFoundTest() {
        // Given
        Long scheduleId = 999L;
        given(scheduleRepository.findById(scheduleId)).willReturn(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                scheduleService.deleteSchedule(scheduleId, 1L, "127.0.0.1"));
    }

    @Test
    @DisplayName("스케줄 삭제 실패 테스트 - 과거 스케줄")
    void deleteScheduleFailPastScheduleTest() {
        // Given
        Long scheduleId = 1L;
        Schedule mockSchedule = Schedule.createSchedule(new User(), null, "과거 스케줄", ScheduleType.DAYOFF, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), 1L, "127.0.0.1");

        given(scheduleRepository.findById(scheduleId)).willReturn(mockSchedule);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                scheduleService.deleteSchedule(scheduleId, 1L, "127.0.0.1"));
    }

    @Test
    @DisplayName("실제 사용 휴가 시간 계산 테스트")
    void calculateRealUsedTest() {
        // Given
        User mockUser = new User();
        Vacation mockVacation = Vacation.createVacation(mockUser, "연차", "연차 휴가", VacationType.BASIC, new BigDecimal("40.0000"), LocalDateTime.now().minusDays(30), LocalDateTime.now().plusDays(30), 1L, "127.0.0.1");
        Schedule mockSchedule = Schedule.createSchedule(mockUser, mockVacation, "5일 연차", ScheduleType.DAYOFF, LocalDateTime.of(2023, 5, 1, 9, 0), LocalDateTime.of(2023, 5, 5, 18, 0), 1L, "127.0.0.1");

        List<LocalDate> holidays = Arrays.asList(LocalDate.of(2023, 5, 3));

        // When
        BigDecimal realUsed = scheduleService.calculateRealUsed(mockSchedule, holidays);

        // Then
        assertThat(realUsed).isEqualTo(new BigDecimal("32.0000")); // 5일 중 주말(1일)과 공휴일(1일)을 제외한 3일 * 8시간 = 24시간
    }
}
