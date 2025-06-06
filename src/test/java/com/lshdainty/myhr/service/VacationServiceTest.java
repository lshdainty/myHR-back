package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.*;
import com.lshdainty.myhr.repository.UserRepositoryImpl;
import com.lshdainty.myhr.repository.VacationRepositoryImpl;
import com.lshdainty.myhr.service.dto.ScheduleServiceDto;
import com.lshdainty.myhr.service.dto.VacationServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("휴가 서비스 테스트")
class VacationServiceTest {
    @Mock
    private MessageSource ms;
    @Mock
    private VacationRepositoryImpl vacationRepositoryImpl;
    @Mock
    private UserRepositoryImpl userRepositoryImpl;
    @Mock
    private UserService userService;
    @Mock
    private ScheduleService scheduleService;
    @InjectMocks
    private VacationService vacationService;

    @Test
    @DisplayName("휴가 추가 테스트 - 성공")
    void addVacationSuccessTest() {
        // Given
        String name = "정기 휴가";
        String desc = "25년 1분기 정기 휴가";
        VacationType type = VacationType.BASIC;
        BigDecimal grantTime = new BigDecimal("4.0000");
        LocalDateTime occurDate = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(LocalDateTime.now().getYear(), 12, 31, 23, 59, 59);

        Long userNo = 1L;
        User user = User.createUser("이서준", "19700723", "9 ~ 6", "ADMIN", "N");
        Vacation vacation = Vacation.createVacation(user, name, desc, type, grantTime, occurDate, expiryDate, 0L, "127.0.0.1");

        // Reflection을 사용하여 id 설정
        setUserNo(user, userNo);

        given(userService.checkUserExist(userNo)).willReturn(user);
        willDoNothing().given(vacationRepositoryImpl).save(any(Vacation.class));

        // When
        Long vacationId = vacationService.addVacation(userNo, name, desc, type, grantTime, occurDate, expiryDate, 0L, "127.0.0.1");

        // Then
        then(userService).should().checkUserExist(userNo);
        then(vacationRepositoryImpl).should().save(any(Vacation.class));
    }

    @Test
    @DisplayName("휴가 추가 테스트 - 실패 (유저 없음)")
    void addVacationFailUserNotFoundTest() {
        // Given
        String name = "정기 휴가";
        String desc = "25년 1분기 정기 휴가";
        VacationType type = VacationType.BASIC;
        BigDecimal grantTime = new BigDecimal("4.0000");
        LocalDateTime occurDate = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(LocalDateTime.now().getYear(), 12, 31, 23, 59, 59);

        Long userNo = 900L;
        given(userService.checkUserExist(userNo)).willThrow(new IllegalArgumentException(""));

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.addVacation(userNo, name, desc, type, grantTime, occurDate, expiryDate,0L, "127.0.0.1"));
        then(userService).should().checkUserExist(userNo);
        then(vacationRepositoryImpl).should(never()).save(any(Vacation.class));
    }

    @Test
    @DisplayName("휴가 추가 테스트 - 실패 (만료일자 이전)")
    void addVacationFailIsBeforeOccurTest() {
        // Given
        String name = "정기 휴가";
        String desc = "25년 1분기 정기 휴가";
        VacationType type = VacationType.BASIC;
        BigDecimal grantTime = new BigDecimal("4.0000");
        LocalDateTime occurDate = LocalDateTime.of(LocalDateTime.now().getYear(), 12, 31, 23, 59, 59);
        LocalDateTime expiryDate = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0, 0);

        Long userNo = 1L;
        User user = User.createUser("이서준", "19700723", "9 ~ 6", "ADMIN", "N");

        // Reflection을 사용하여 id 설정
        setUserNo(user, userNo);
        given(userService.checkUserExist(userNo)).willReturn(user);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.addVacation(userNo, name, desc, type, grantTime, occurDate, expiryDate,0L, "127.0.0.1"));
        then(userService).should().checkUserExist(userNo);
        then(vacationRepositoryImpl).should(never()).save(any(Vacation.class));
    }

    @Test
    @DisplayName("단일 유저 휴가 조회 테스트 - 성공")
    void findVacationsByUserTest() {
        // Given
        Long userNo = 1L;
        User user = User.createUser("이서준", "19700723", "9 ~ 6", "ADMIN", "N");

        LocalDateTime now = LocalDateTime.now();
        given(vacationRepositoryImpl.findVacationsByUserNo(userNo)).willReturn(List.of(
                Vacation.createVacation(user, "정기 휴가", "25년 1분기 정기 휴가", VacationType.BASIC, new BigDecimal("4.0000"), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), 0L, "127.0.0.1"),
                Vacation.createVacation(user, "출산 휴가", "출산 추가 휴가 부여", VacationType.ADDED, new BigDecimal("10.0000"), LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0), LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59).plusMonths(6), 0L, "127.0.0.1"),
                Vacation.createVacation(user, "OT 정산", "월마감 지원", VacationType.ADDED, new BigDecimal("0.3750"), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(2025, 1, 31, 23, 59, 59), 0L, "127.0.0.1")
        ));

        // When
        List<Vacation> vacations = vacationService.findVacationsByUser(userNo);

        // Then
        then(vacationRepositoryImpl).should().findVacationsByUserNo(userNo);
        assertThat(vacations).hasSize(3);
        assertThat(vacations)
                .extracting("name")
                .containsExactlyInAnyOrder("정기 휴가", "출산 휴가", "OT 정산");
    }

    @Test
    @DisplayName("단일 유저 휴가 조회 테스트 - 성공 (빈 결과)")
    void findVacationsByUserEmptyResultTest() {
        // Given
        Long userNo = 1L;
        given(vacationRepositoryImpl.findVacationsByUserNo(userNo)).willReturn(Collections.emptyList());

        // When
        List<Vacation> vacations = vacationService.findVacationsByUser(userNo);

        // Then
        then(vacationRepositoryImpl).should().findVacationsByUserNo(userNo);
        assertThat(vacations).isEmpty();
    }

    @Test
    @DisplayName("유저별 휴가 조회 테스트 - 성공")
    void findVacationsByUserGroupTest() {
        // Given
        User userA = User.createUser("이서준", "19700723", "ADMIN", "9 ~ 6", "N");
        User userB = User.createUser("김서연", "19701026", "BP", "8 ~ 5", "N");
        User userC = User.createUser("김지후", "19740115", "BP", "10 ~ 7", "Y");

        LocalDateTime now = LocalDateTime.now();
        Vacation v1 = Vacation.createVacation(userA, "정기 휴가", "25년 1분기 정기 휴가", VacationType.BASIC, new BigDecimal("4.0000"), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), 0L, "127.0.0.1");
        Vacation v2 = Vacation.createVacation(userA, "출산 휴가", "출산 추가 휴가 부여", VacationType.ADDED, new BigDecimal("10.0000"), LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0), LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59).plusMonths(6), 0L, "127.0.0.1");
        Vacation v3 = Vacation.createVacation(userA, "OT 정산", "월마감 지원", VacationType.ADDED, new BigDecimal("0.3750"), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(2025, 1, 31, 23, 59, 59), 0L, "127.0.0.1");
        Vacation v4 = Vacation.createVacation(userB, "정기 휴가", "25년 1분기 정기 휴가", VacationType.BASIC, new BigDecimal("4.0000"), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), 0L, "127.0.0.1");

        given(userRepositoryImpl.findUsersWithVacations()).willReturn(List.of(
                userA, userB, userC
        ));

        // When
        List<User> users = vacationService.findVacationsByUserGroup();

        // Then
        then(userRepositoryImpl).should().findUsersWithVacations();
        assertThat(users).hasSize(3);
        assertThat(users)
                .filteredOn(u -> u.getName().equals("이서준"))
                .filteredOn(v -> v.getVacations().size() == 3);
        assertThat(users)
                .filteredOn(u -> u.getName().equals("김서연"))
                .filteredOn(v -> v.getVacations().size() == 1);
        assertThat(users)
                .filteredOn(u -> u.getName().equals("김지후"))
                .filteredOn(v -> v.getVacations().isEmpty());
    }

    @Test
    @DisplayName("휴가 수정 테스트 - 성공")
    void editVacationSuccessTest() {
        // Given
        Long userNo = 1L;
        User user = User.createUser("이서준", "19700723", "9 ~ 6", "ADMIN", "N");

        String name = "정기 휴가";
        String desc = "25년 1분기 정기 휴가";
        VacationType type = VacationType.BASIC;
        BigDecimal oldGrantTime = new BigDecimal("4.0000");
        LocalDateTime occurDate = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(LocalDateTime.now().getYear(), 12, 31, 23, 59, 59);

        Vacation oldVacation = Vacation.createVacation(user, name, desc, type, oldGrantTime, occurDate, expiryDate, 0L, "127.0.0.1");
        Long oldVacationId = 1L;
        setVacationId(oldVacation, oldVacationId);

        BigDecimal newGrantTime = new BigDecimal("3.0000");
        Vacation newVacation = Vacation.createVacation(user, name, desc, type, newGrantTime, occurDate, expiryDate, 0L, "127.0.0.1");
        Long newVacationId = 2L;
        setVacationId(newVacation, newVacationId);

        given(vacationRepositoryImpl.findById(oldVacationId)).willReturn(oldVacation);
        given(userService.checkUserExist(userNo)).willReturn(user);
        willDoNothing().given(vacationRepositoryImpl).save(any(Vacation.class));
        given(vacationRepositoryImpl.findById(null)).willReturn(newVacation);

        // When
        Vacation result = vacationService.editVacation(oldVacationId, userNo, null, null, null,
                newGrantTime, null, null, 0L, "127.0.0.1");

        // Then
        then(vacationRepositoryImpl).should().findById(oldVacationId);
        then(userService).should().checkUserExist(userNo);
        then(vacationRepositoryImpl).should().save(any(Vacation.class));

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(newVacationId);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getDesc()).isEqualTo(desc);
        assertThat(result.getType()).isEqualTo(type);
        assertThat(result.getGrantTime()).isEqualTo(newGrantTime);
        assertThat(result.getOccurDate()).isEqualTo(occurDate);
        assertThat(result.getExpiryDate()).isEqualTo(expiryDate);
    }

    @Test
    @DisplayName("휴가 수정 테스트 - 실패 (휴가 없음)")
    void editVacationFailVacationNotFoundTest() {
        // Given
        Long vacationId = 900L;
        Long userNo = 1L;
        given(vacationRepositoryImpl.findById(vacationId)).willReturn(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.editVacation(vacationId, userNo, null, null, null, new BigDecimal("3.0000"), null, null, 0L, "127.0.0.1"));

        then(vacationRepositoryImpl).should().findById(vacationId);
        then(userService).should(never()).checkUserExist(userNo);
    }

    @Test
    @DisplayName("휴가 수정 테스트 - 실패 (사용자 없음)")
    void editVacationFailUserNotFoundTest() {
        // Given
        Long userNo = 900L;
        User user = User.createUser("이서준", "19700723", "9 ~ 6", "ADMIN", "N");

        String name = "정기 휴가";
        String desc = "25년 1분기 정기 휴가";
        VacationType type = VacationType.BASIC;
        BigDecimal oldGrantTime = new BigDecimal("4.0000");
        LocalDateTime occurDate = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0, 0);;
        LocalDateTime expiryDate = LocalDateTime.of(LocalDateTime.now().getYear(), 12, 31, 23, 59, 59);

        Vacation oldVacation = Vacation.createVacation(user, name, desc, type, oldGrantTime, occurDate, expiryDate, 0L, "127.0.0.1");
        Long oldVacationId = 1L;
        setVacationId(oldVacation, oldVacationId);

        given(vacationRepositoryImpl.findById(oldVacationId)).willReturn(oldVacation);
        given(userService.checkUserExist(userNo)).willThrow(new IllegalArgumentException(""));

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.editVacation(oldVacationId, userNo, null, null, null, new BigDecimal("3.0000"), null, null, 0L, "127.0.0.1"));

        then(vacationRepositoryImpl).should().findById(oldVacationId);
        then(userService).should().checkUserExist(userNo);
    }

    @Test
    @DisplayName("휴가 수정 테스트 - 실패 (만료일자 이전)")
    void editVacationFailIsBeforeOccurTest() {
        // Given
        Long userNo = 1L;
        User user = User.createUser("이서준", "19700723", "9 ~ 6", "ADMIN", "N");

        String name = "정기 휴가";
        String desc = "25년 1분기 정기 휴가";
        VacationType type = VacationType.BASIC;
        BigDecimal oldGrantTime = new BigDecimal("4.0000");
        LocalDateTime occurDate = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(LocalDateTime.now().getYear(), 12, 31, 23, 59, 59);

        Vacation oldVacation = Vacation.createVacation(user, name, desc, type, oldGrantTime, occurDate, expiryDate, 0L, "127.0.0.1");
        Long oldVacationId = 1L;
        setVacationId(oldVacation, oldVacationId);

        given(vacationRepositoryImpl.findById(oldVacationId)).willReturn(oldVacation);
        given(userService.checkUserExist(userNo)).willReturn(user);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.editVacation(oldVacationId, userNo, null, null, null, new BigDecimal("3.0000"), LocalDateTime.of(LocalDateTime.now().getYear(), 12, 31, 23, 59, 59), LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0, 0), 0L, "127.0.0.1"));

        then(vacationRepositoryImpl).should().findById(oldVacationId);
        then(userService).should().checkUserExist(userNo);
        then(vacationRepositoryImpl).should(never()).save(any(Vacation.class));
    }

    @Test
    @DisplayName("휴가 삭제 테스트 - 성공")
    void deleteVacationSuccessTest() {
        // Given
        Long vacationId = 1L;
        User user = User.createUser("이서준", "19700723", "9 ~ 6", "ADMIN", "N");

        String name = "정기 휴가";
        String desc = "25년 1분기 정기 휴가";
        VacationType type = VacationType.BASIC;
        BigDecimal oldGrantTime = new BigDecimal("4.0000");
        LocalDateTime occurDate = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0, 0);;
        LocalDateTime expiryDate = LocalDateTime.of(LocalDateTime.now().getYear(), 12, 31, 23, 59, 59);
        Vacation vacation = Vacation.createVacation(user, name, desc, type, oldGrantTime, occurDate, expiryDate, 0L, "127.0.0.1");

        given(vacationRepositoryImpl.findById(vacationId)).willReturn(vacation);

        // When
        vacationService.deleteVacation(vacationId, 0L, "127.0.0.1");

        // Then
        then(vacationRepositoryImpl).should().findById(vacationId);
        assertThat(vacation.getDelYN()).isEqualTo("Y");
    }

    @Test
    @DisplayName("휴가 삭제 테스트 - 실패 (휴가 없음)")
    void deleteVacationFailVacationNotFoundTest() {
        // Given
        Long vacationId = 900L;

        given(vacationRepositoryImpl.findById(vacationId)).willReturn(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.deleteVacation(vacationId, 0L, "127.0.0.1"));

        then(vacationRepositoryImpl).should().findById(vacationId);
    }

    @Test
    @DisplayName("등록 가능한 휴가 조회 테스트 - 성공")
    void checkPossibleVacationSuccessTest() {
        // given
        LocalDateTime standardTime = LocalDateTime.now();

        Long userNo = 1L;
        User user = User.createUser("이서준", "19700723", "9 ~ 6", "ADMIN", "N");

        Vacation v1 = Vacation.createVacation(user, "정기 휴가", "25년 1분기 정기 휴가", VacationType.BASIC, new BigDecimal("4.0000"), LocalDateTime.of(standardTime.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(standardTime.getYear(), 12, 31, 23, 59, 59), 0L, "127.0.0.1");
        Vacation v2 = Vacation.createVacation(user, "출산 휴가", "출산 추가 휴가 부여", VacationType.ADDED, new BigDecimal("10.0000"), LocalDateTime.of(standardTime.getYear(), standardTime.getMonth(), standardTime.getDayOfMonth(), 0, 0, 0), LocalDateTime.of(standardTime.getYear(), standardTime.getMonth(), standardTime.getDayOfMonth(), 23, 59, 59).plusMonths(6), 0L, "127.0.0.1");
        Vacation v3 = Vacation.createVacation(user, "OT 정산", "월마감 지원", VacationType.ADDED, new BigDecimal("0.3750"), LocalDateTime.of(standardTime.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(2025, 1, 31, 23, 59, 59), 0L, "127.0.0.1");
        Vacation v4 = Vacation.createVacation(user, "정기 휴가", "25년 1분기 정기 휴가", VacationType.BASIC, new BigDecimal("4.0000"), LocalDateTime.of(standardTime.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(standardTime.getYear(), 12, 31, 23, 59, 59), 0L, "127.0.0.1");

        Schedule s1 = Schedule.createSchedule(user, v1, "휴가", ScheduleType.DAYOFF, LocalDateTime.of(LocalDateTime.now().getYear(), 1, 20, 0, 0, 0), LocalDateTime.of(LocalDateTime.now().getYear(), 1, 20, 23, 59, 59), 0L, "127.0.0.1");

        ScheduleServiceDto scheduleDto = ScheduleServiceDto.builder()
                .user(user)
                .vacation(v1)
                .type(ScheduleType.DAYOFF)
                .realUsedTime(new BigDecimal("1.0000"))
                .build();

        given(userService.checkUserExist(userNo)).willReturn(user);
        given(vacationRepositoryImpl.findVacationsByParameterTimeWithSchedules(userNo, standardTime)).willReturn(List.of(v1, v2, v3, v4));
        setVacationSchedules(v1, List.of(s1));
        given(scheduleService.convertRealUsedTimeDto(any())).willReturn(List.of(scheduleDto));

        // when
        List<VacationServiceDto> findVacation = vacationService.checkPossibleVacations(userNo, standardTime);

        // then
        then(userService).should().checkUserExist(userNo);
        then(vacationRepositoryImpl).should().findVacationsByParameterTimeWithSchedules(userNo, standardTime);
        assertThat(findVacation).hasSize(4);
        assertThat(findVacation.get(0).getGrantTime()).isEqualTo(new BigDecimal("4.0000"));
        assertThat(findVacation.get(0).getUsedTime()).isEqualTo(new BigDecimal("1.0000"));
        assertThat(findVacation.get(0).getRemainTime()).isEqualTo(new BigDecimal("3.0000"));
    }

    @Test
    @DisplayName("등록 가능한 휴가 조회 테스트 - 성공 (사용 가능 시간 없음)")
    void checkPossibleVacationsNoAvailableTest() {
        // given
        LocalDateTime standardTime = LocalDateTime.now();

        Long userNo = 1L;
        User user = User.createUser("이서준", "19700723", "9 ~ 6", "ADMIN", "N");

        Vacation v1 = Vacation.createVacation(user, "정기 휴가", "25년 1분기 정기 휴가", VacationType.BASIC, new BigDecimal("4.0000"), LocalDateTime.of(standardTime.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(standardTime.getYear(), 12, 31, 23, 59, 59), 0L, "127.0.0.1");
        Vacation v2 = Vacation.createVacation(user, "출산 휴가", "출산 추가 휴가 부여", VacationType.ADDED, new BigDecimal("10.0000"), LocalDateTime.of(standardTime.getYear(), standardTime.getMonth(), standardTime.getDayOfMonth(), 0, 0, 0), LocalDateTime.of(standardTime.getYear(), standardTime.getMonth(), standardTime.getDayOfMonth(), 23, 59, 59).plusMonths(6), 0L, "127.0.0.1");
        Vacation v3 = Vacation.createVacation(user, "OT 정산", "월마감 지원", VacationType.ADDED, new BigDecimal("0.3750"), LocalDateTime.of(standardTime.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(2025, 1, 31, 23, 59, 59), 0L, "127.0.0.1");
        Vacation v4 = Vacation.createVacation(user, "정기 휴가", "25년 1분기 정기 휴가", VacationType.BASIC, new BigDecimal("4.0000"), LocalDateTime.of(standardTime.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(standardTime.getYear(), 12, 31, 23, 59, 59), 0L, "127.0.0.1");

        Schedule s1 = Schedule.createSchedule(user, v1, "휴가", ScheduleType.DAYOFF, LocalDateTime.of(LocalDateTime.now().getYear(), 1, 20, 0, 0, 0), LocalDateTime.of(LocalDateTime.now().getYear(), 1, 23, 23, 59, 59), 0L, "127.0.0.1");

        ScheduleServiceDto scheduleDto = ScheduleServiceDto.builder()
                .user(user)
                .vacation(v1)
                .type(ScheduleType.DAYOFF)
                .realUsedTime(new BigDecimal("4.0000"))
                .build();

        given(userService.checkUserExist(userNo)).willReturn(user);
        given(vacationRepositoryImpl.findVacationsByParameterTimeWithSchedules(userNo, standardTime)).willReturn(List.of(v1, v2, v3, v4));
        setVacationSchedules(v1, List.of(s1));
        given(scheduleService.convertRealUsedTimeDto(any())).willReturn(List.of(scheduleDto));

        // When
        List<VacationServiceDto> findVacation = vacationService.checkPossibleVacations(userNo, standardTime);

        // Then
        then(userService).should().checkUserExist(userNo);
        then(vacationRepositoryImpl).should().findVacationsByParameterTimeWithSchedules(userNo, standardTime);
        assertThat(findVacation).hasSize(3);
        assertThat(findVacation.get(0).getGrantTime()).isEqualTo(new BigDecimal("10.0000"));
        assertThat(findVacation.get(0).getUsedTime()).isEqualTo(new BigDecimal("0.0000"));
        assertThat(findVacation.get(0).getRemainTime()).isEqualTo(new BigDecimal("10.0000"));
    }

    @Test
    @DisplayName("등록 가능한 휴가 조회 테스트 - 성공 (스케줄 없음)")
    void checkPossibleVacationSuccessNoSchedulesTest() {
        // Given
        LocalDateTime standardTime = LocalDateTime.now();

        Long userNo = 1L;
        User user = User.createUser("이서준", "19700723", "9 ~ 6", "ADMIN", "N");

        Vacation v1 = Vacation.createVacation(user, "정기 휴가", "25년 1분기 정기 휴가", VacationType.BASIC, new BigDecimal("4.0000"), LocalDateTime.of(standardTime.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(standardTime.getYear(), 12, 31, 23, 59, 59), 0L, "127.0.0.1");
        Vacation v2 = Vacation.createVacation(user, "출산 휴가", "출산 추가 휴가 부여", VacationType.ADDED, new BigDecimal("10.0000"), LocalDateTime.of(standardTime.getYear(), standardTime.getMonth(), standardTime.getDayOfMonth(), 0, 0, 0), LocalDateTime.of(standardTime.getYear(), standardTime.getMonth(), standardTime.getDayOfMonth(), 23, 59, 59).plusMonths(6), 0L, "127.0.0.1");
        Vacation v3 = Vacation.createVacation(user, "OT 정산", "월마감 지원", VacationType.ADDED, new BigDecimal("0.3750"), LocalDateTime.of(standardTime.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(2025, 1, 31, 23, 59, 59), 0L, "127.0.0.1");
        Vacation v4 = Vacation.createVacation(user, "정기 휴가", "25년 1분기 정기 휴가", VacationType.BASIC, new BigDecimal("4.0000"), LocalDateTime.of(standardTime.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(standardTime.getYear(), 12, 31, 23, 59, 59), 0L, "127.0.0.1");

        given(userService.checkUserExist(userNo)).willReturn(user);
        given(vacationRepositoryImpl.findVacationsByParameterTimeWithSchedules(userNo, standardTime)).willReturn(List.of(v1, v2, v3, v4));

        // When
        List<VacationServiceDto> findVacation = vacationService.checkPossibleVacations(userNo, standardTime);

        // Then
        then(userService).should().checkUserExist(userNo);
        then(vacationRepositoryImpl).should().findVacationsByParameterTimeWithSchedules(userNo, standardTime);
        assertThat(findVacation).hasSize(4);
        assertThat(findVacation.get(0).getGrantTime()).isEqualTo(new BigDecimal("4.0000"));
        assertThat(findVacation.get(0).getUsedTime()).isEqualTo(new BigDecimal("0.0000"));
        assertThat(findVacation.get(0).getRemainTime()).isEqualTo(new BigDecimal("4.0000"));
    }

    // 테스트 헬퍼 메서드
    private void setUserNo(User user, Long no) {
        try {
            java.lang.reflect.Field field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, no);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setVacationId(Vacation vacation, Long id) {
        try {
            java.lang.reflect.Field field = Vacation.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(vacation, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setVacationSchedules(Vacation vacation, List<Schedule> schedules) {
        try {
            java.lang.reflect.Field field = Vacation.class.getDeclaredField("schedules");
            field.setAccessible(true);
            field.set(vacation, schedules);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
