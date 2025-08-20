package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.*;
import com.lshdainty.myhr.repository.HolidayRepositoryImpl;
import com.lshdainty.myhr.repository.UserRepositoryImpl;
import com.lshdainty.myhr.repository.VacationHistoryRepositoryImpl;
import com.lshdainty.myhr.repository.VacationRepositoryImpl;
import com.lshdainty.myhr.api.dto.VacationStatsDto;
import com.lshdainty.myhr.service.dto.VacationServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("휴가 서비스 테스트")
class VacationServiceTest {
    // 삭제하지 말 것 (NullpointException 발생)
    @Mock
    private MessageSource ms;
    @Mock
    private VacationRepositoryImpl vacationRepositoryImpl;
    @Mock
    private VacationHistoryRepositoryImpl vacationHistoryRepositoryImpl;
    @Mock
    private UserRepositoryImpl userRepositoryImpl;
    @Mock
    private UserService userService;
    @Mock
    private HolidayRepositoryImpl holidayRepositoryImpl;

    @InjectMocks
    private VacationService vacationService;

    @Test
    @DisplayName("연차 추가 테스트 - 성공 (기존 휴가 업데이트)")
    void registExistAnnualSuccessTest() {
        // Given
        String userId = "test1";
        String desc = "1분기 정기 휴가";
        VacationType type = VacationType.ANNUAL;
        BigDecimal grantTime = new BigDecimal("4.0000");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime occurDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59);

        User user = User.createUser("test1");
        Vacation vacation = Vacation.createVacation(user, type, grantTime,
                LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0),
                LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59),
                "", "127.0.0.1");

        given(userService.checkUserExist(userId)).willReturn(user);
        given(vacationRepositoryImpl.findVacationByTypeWithYear(userId, type, String.valueOf(now.getYear())))
                .willReturn(Optional.of(vacation));
        willDoNothing().given(vacationHistoryRepositoryImpl).save(any(VacationHistory.class));

        // When
        vacationService.registVacation(
                VacationServiceDto.builder()
                        .userId(userId)
                        .desc(desc)
                        .type(type)
                        .grantTime(grantTime)
                        .occurDate(occurDate)
                        .expiryDate(expiryDate)
                        .build(),
                "", "127.0.0.1"
        );

        // Then
        then(userService).should().checkUserExist(userId);
        then(vacationRepositoryImpl).should().findVacationByTypeWithYear(userId, type, String.valueOf(now.getYear()));
        then(vacationHistoryRepositoryImpl).should().save(any(VacationHistory.class));

        assertThat(vacation.getRemainTime()).isEqualTo(grantTime.multiply(new BigDecimal("2")));
    }

    @Test
    @DisplayName("연차 추가 테스트 - 성공 (신규 휴가 등록)")
    void registNewAnnualSuccessTest() {
        // Given
        String userId = "test1";
        String desc = "1분기 정기 휴가";
        VacationType type = VacationType.ANNUAL;
        BigDecimal grantTime = new BigDecimal("4.0000");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime occurDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59);

        User user = User.createUser("test1");

        given(userService.checkUserExist(userId)).willReturn(user);
        given(vacationRepositoryImpl.findVacationByTypeWithYear(userId, type, String.valueOf(now.getYear())))
                .willReturn(Optional.empty());
        willDoNothing().given(vacationRepositoryImpl).save(any(Vacation.class));
        willDoNothing().given(vacationHistoryRepositoryImpl).save(any(VacationHistory.class));

        // When
        vacationService.registVacation(
                VacationServiceDto.builder()
                        .userId(userId)
                        .desc(desc)
                        .type(type)
                        .grantTime(grantTime)
                        .occurDate(occurDate)
                        .expiryDate(expiryDate)
                        .build(),
                "", "127.0.0.1"
        );

        // Then
        then(userService).should().checkUserExist(userId);
        then(vacationRepositoryImpl).should().findVacationByTypeWithYear(userId, type, String.valueOf(now.getYear()));
        then(vacationRepositoryImpl).should().save(argThat(vacation -> {
            assertThat(vacation.getOccurDate().getYear()).isEqualTo(occurDate.getYear());
            return true;
        }));
        then(vacationRepositoryImpl).should().save(argThat(vacation -> {
            assertThat(vacation.getExpiryDate().getYear()).isEqualTo(expiryDate.getYear());
            return true;
        }));
        then(vacationHistoryRepositoryImpl).should().save(any(VacationHistory.class));
    }

    @Test
    @DisplayName("출산 추가 테스트 - 성공")
    void registNewMaternitySuccessTest() {
        // Given
        String userId = "test1";
        String desc = "출산 휴가";
        VacationType type = VacationType.MATERNITY;
        BigDecimal grantTime = new BigDecimal("10.0000");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime occurDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(now.getYear(), 6, 1, 23, 59, 59);

        User user = User.createUser("test1");

        given(userService.checkUserExist(userId)).willReturn(user);
        willDoNothing().given(vacationRepositoryImpl).save(any(Vacation.class));
        willDoNothing().given(vacationHistoryRepositoryImpl).save(any(VacationHistory.class));

        // When
        vacationService.registVacation(
                VacationServiceDto.builder()
                        .userId(userId)
                        .desc(desc)
                        .type(type)
                        .grantTime(grantTime)
                        .occurDate(occurDate)
                        .expiryDate(expiryDate)
                        .build(),
                "", "127.0.0.1"
        );

        // Then
        then(userService).should().checkUserExist(userId);
        then(vacationRepositoryImpl).should().save(any(Vacation.class));
        then(vacationHistoryRepositoryImpl).should().save(any(VacationHistory.class));
    }

    @Test
    @DisplayName("결혼 추가 테스트 - 성공")
    void registNewWeddingSuccessTest() {
        // Given
        String userId = "test1";
        String desc = "결혼 휴가";
        VacationType type = VacationType.WEDDING;
        BigDecimal grantTime = new BigDecimal("5.0000");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime occurDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(now.getYear(), 6, 1, 23, 59, 59);

        User user = User.createUser("test1");

        given(userService.checkUserExist(userId)).willReturn(user);
        willDoNothing().given(vacationRepositoryImpl).save(any(Vacation.class));
        willDoNothing().given(vacationHistoryRepositoryImpl).save(any(VacationHistory.class));

        // When
        vacationService.registVacation(
                VacationServiceDto.builder()
                        .userId(userId)
                        .desc(desc)
                        .type(type)
                        .grantTime(grantTime)
                        .occurDate(occurDate)
                        .expiryDate(expiryDate)
                        .build(),
                "", "127.0.0.1"
        );

        // Then
        then(userService).should().checkUserExist(userId);
        then(vacationRepositoryImpl).should().save(any(Vacation.class));
        then(vacationHistoryRepositoryImpl).should().save(any(VacationHistory.class));
    }

    @Test
    @DisplayName("상조 추가 테스트 - 성공")
    void registNewBereavementSuccessTest() {
        // Given
        String userId = "test1";
        String desc = "상조 휴가";
        VacationType type = VacationType.BEREAVEMENT;
        BigDecimal grantTime = new BigDecimal("3.0000");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime occurDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(now.getYear(), 6, 1, 23, 59, 59);

        User user = User.createUser("test1");

        given(userService.checkUserExist(userId)).willReturn(user);
        willDoNothing().given(vacationRepositoryImpl).save(any(Vacation.class));
        willDoNothing().given(vacationHistoryRepositoryImpl).save(any(VacationHistory.class));

        // When
        vacationService.registVacation(
                VacationServiceDto.builder()
                        .userId(userId)
                        .desc(desc)
                        .type(type)
                        .grantTime(grantTime)
                        .occurDate(occurDate)
                        .expiryDate(expiryDate)
                        .build(),
                "", "127.0.0.1"
        );

        // Then
        then(userService).should().checkUserExist(userId);
        then(vacationRepositoryImpl).should().save(any(Vacation.class));
        then(vacationHistoryRepositoryImpl).should().save(any(VacationHistory.class));
    }

    @Test
    @DisplayName("OT 추가 테스트 - 성공 (기존 휴가 업데이트)")
    void registExistOvertimeSuccessTest() {
        // Given
        String userId = "test1";
        String desc = "연장근무 휴가추가";
        VacationType type = VacationType.OVERTIME;
        BigDecimal grantTime = new BigDecimal("0.1250");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime occurDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59);

        User user = User.createUser("test1");
        Vacation vacation = Vacation.createVacation(user, type, grantTime,
                LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0),
                LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59),
                "", "127.0.0.1");

        given(userService.checkUserExist(userId)).willReturn(user);
        given(vacationRepositoryImpl.findVacationByTypeWithYear(userId, type, String.valueOf(now.getYear())))
                .willReturn(Optional.of(vacation));
        willDoNothing().given(vacationHistoryRepositoryImpl).save(any(VacationHistory.class));

        // When
        vacationService.registVacation(
                VacationServiceDto.builder()
                        .userId(userId)
                        .desc(desc)
                        .type(type)
                        .grantTime(grantTime)
                        .occurDate(occurDate)
                        .expiryDate(expiryDate)
                        .build(),
                "", "127.0.0.1"
        );

        // Then
        then(userService).should().checkUserExist(userId);
        then(vacationRepositoryImpl).should().findVacationByTypeWithYear(userId, type, String.valueOf(now.getYear()));
        then(vacationHistoryRepositoryImpl).should().save(any(VacationHistory.class));

        assertThat(vacation.getRemainTime()).isEqualTo(grantTime.multiply(new BigDecimal("2")));
    }

    @Test
    @DisplayName("OT 추가 테스트 - 성공 (신규 휴가 등록)")
    void registNewOvertimeSuccessTest() {
        // Given
        String userId = "test1";
        String desc = "연장근무 휴가추가";
        VacationType type = VacationType.OVERTIME;
        BigDecimal grantTime = new BigDecimal("0.1250");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime occurDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59);

        User user = User.createUser("test1");

        given(userService.checkUserExist(userId)).willReturn(user);
        given(vacationRepositoryImpl.findVacationByTypeWithYear(userId, type, String.valueOf(now.getYear())))
                .willReturn(Optional.empty());
        willDoNothing().given(vacationRepositoryImpl).save(any(Vacation.class));
        willDoNothing().given(vacationHistoryRepositoryImpl).save(any(VacationHistory.class));

        // When
        vacationService.registVacation(
                VacationServiceDto.builder()
                        .userId(userId)
                        .desc(desc)
                        .type(type)
                        .grantTime(grantTime)
                        .occurDate(occurDate)
                        .expiryDate(expiryDate)
                        .build(),
                "", "127.0.0.1"
        );

        // Then
        then(userService).should().checkUserExist(userId);
        then(vacationRepositoryImpl).should().findVacationByTypeWithYear(userId, type, String.valueOf(now.getYear()));
        then(vacationRepositoryImpl).should().save(argThat(vacation -> {
            assertThat(vacation.getOccurDate().getYear()).isEqualTo(occurDate.getYear());
            return true;
        }));
        then(vacationRepositoryImpl).should().save(argThat(vacation -> {
            assertThat(vacation.getExpiryDate().getYear()).isEqualTo(expiryDate.getYear());
            return true;
        }));
        then(vacationHistoryRepositoryImpl).should().save(any(VacationHistory.class));
    }

    @Test
    @DisplayName("휴가 사용 추가 테스트 - 성공")
    void useVacationSuccessTest() {
        // Given
        String userId = "test1";
        Long vacationId = 1L;

        String desc = "연차";
        VacationTimeType timeType = VacationTimeType.DAYOFF;
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 6, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 6, 9, 23, 59, 59);

        User user = User.createUser("test1");
        Vacation vacation = Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("4.0000"),
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59),
                "", "127.0.0.1");
        Holiday holiday = Holiday.createHoliday("현충일", "20250606", HolidayType.PUBLIC);

        given(userService.checkUserExist(userId)).willReturn(user);
        given(vacationRepositoryImpl.findById(vacationId)).willReturn(Optional.of(vacation));
        given(holidayRepositoryImpl.findHolidaysByStartEndDateWithType(
                startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                HolidayType.PUBLIC
        )).willReturn(List.of(holiday));

        // When
        vacationService.useVacation(
                VacationServiceDto.builder()
                        .userId(userId)
                        .id(vacationId)
                        .desc(desc)
                        .timeType(timeType)
                        .startDate(startDate)
                        .endDate(endDate)
                        .build(),
                "", "127.0.0.1"
        );

        // Then
        then(userService).should().checkUserExist(userId);
        then(vacationRepositoryImpl).should().findById(vacationId);
        then(holidayRepositoryImpl).should().findHolidaysByStartEndDateWithType(
                startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                HolidayType.PUBLIC
        );
        then(vacationHistoryRepositoryImpl).should().save(any(VacationHistory.class));
        assertThat(vacation.getRemainTime()).isEqualTo(new BigDecimal("3.0000"));
    }

    @Test
    @DisplayName("휴가 사용 추가 테스트 - 실패 (휴가 찾기 실패)")
    void useVacationFailTestNotFoundVacation() {
        // Given
        String userId = "test1";
        Long vacationId = 1L;

        String desc = "연차";
        VacationTimeType timeType = VacationTimeType.DAYOFF;
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 6, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 6, 9, 23, 59, 59);

        User user = User.createUser("test1");

        given(userService.checkUserExist(userId)).willReturn(user);

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.useVacation(
                        VacationServiceDto.builder()
                                .userId(userId)
                                .id(vacationId)
                                .desc(desc)
                                .timeType(timeType)
                                .startDate(startDate)
                                .endDate(endDate)
                                .build(),
                        "", "127.0.0.1"
                ));
    }

    @Test
    @DisplayName("휴가 사용 추가 테스트 - 실패 (Start, End 시간 반대)")
    void useVacationFailTestReverseStartEndDate() {
        // Given
        String userId = "test1";
        Long vacationId = 1L;

        String desc = "연차";
        VacationTimeType timeType = VacationTimeType.DAYOFF;
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 9, 23, 59, 59);
        LocalDateTime endDate = LocalDateTime.of(2025, 6, 9, 0, 0, 0);

        User user = User.createUser("test1");
        Vacation vacation = Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("4.0000"),
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59),
                "", "127.0.0.1");

        given(userService.checkUserExist(userId)).willReturn(user);
        given(vacationRepositoryImpl.findById(vacationId)).willReturn(Optional.of(vacation));

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.useVacation(
                        VacationServiceDto.builder()
                                .userId(userId)
                                .id(vacationId)
                                .desc(desc)
                                .timeType(timeType)
                                .startDate(startDate)
                                .endDate(endDate)
                                .build(),
                        "", "127.0.0.1"
                ));
    }

    @Test
    @DisplayName("휴가 사용 추가 테스트 - 실패 (유연근무제)")
    void useVacationFailTestMismatchUserWorkTime() {
        // Given
        String userId = "test1";
        Long vacationId = 1L;

        String desc = "1시간 휴가";
        VacationTimeType timeType = VacationTimeType.ONETIMEOFF;
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 9, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 6, 9, 9, 0, 0);

        User user = User.createUser("", "", "이서준", "", "19700723", "ADMIN", "9 ~ 6", "N");
        Vacation vacation = Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("4.0000"),
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59),
                "", "127.0.0.1");

        given(userService.checkUserExist(userId)).willReturn(user);
        given(vacationRepositoryImpl.findById(vacationId)).willReturn(Optional.of(vacation));

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.useVacation(
                        VacationServiceDto.builder()
                                .userId(userId)
                                .id(vacationId)
                                .desc(desc)
                                .timeType(timeType)
                                .startDate(startDate)
                                .endDate(endDate)
                                .build(),
                        "", "127.0.0.1"
                ));
    }

    @Test
    @DisplayName("휴가 사용 추가 테스트 - 실패 (잔여 휴가 부족)")
    void useVacationFailTestNotEnoughRemainTime() {
        // Given
        String userId = "test1";
        Long vacationId = 1L;

        String desc = "연차";
        VacationTimeType timeType = VacationTimeType.DAYOFF;
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 9, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 6, 9, 23, 59, 59);

        User user = User.createUser("test1");
        Vacation vacation = Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("0.0000"),
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59),
                "", "127.0.0.1");

        given(userService.checkUserExist(userId)).willReturn(user);
        given(vacationRepositoryImpl.findById(vacationId)).willReturn(Optional.of(vacation));
        given(holidayRepositoryImpl.findHolidaysByStartEndDateWithType(
                startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                HolidayType.PUBLIC
        )).willReturn(List.of());

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.useVacation(
                        VacationServiceDto.builder()
                                .userId(userId)
                                .id(vacationId)
                                .desc(desc)
                                .timeType(timeType)
                                .startDate(startDate)
                                .endDate(endDate)
                                .build(),
                        "", "127.0.0.1"
                ));
    }

    @Test
    @DisplayName("단일 유저 휴가 조회 테스트 - 성공")
    void getUserVacationsSuccessTest() {
        // Given
        String userId = "test1";
        User user = User.createUser("test1");

        LocalDateTime now = LocalDateTime.now();
        given(vacationRepositoryImpl.findVacationsByUserId(userId)).willReturn(List.of(
                Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("4.0000"), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), "", "127.0.0.1"),
                Vacation.createVacation(user, VacationType.MATERNITY, new BigDecimal("10.0000"), LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0), LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59).plusMonths(6), "", "127.0.0.1"),
                Vacation.createVacation(user, VacationType.OVERTIME, new BigDecimal("0.3750"), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), "", "127.0.0.1")
        ));

        // When
        List<Vacation> vacations = vacationService.getUserVacations(userId);

        // Then
        then(vacationRepositoryImpl).should().findVacationsByUserId(userId);
        assertThat(vacations).hasSize(3);
        assertThat(vacations)
                .extracting("type")
                .containsExactlyInAnyOrder(VacationType.ANNUAL, VacationType.MATERNITY, VacationType.OVERTIME);
    }

    @Test
    @DisplayName("유저별 휴가 조회 테스트 - 성공")
    void getUserGroupVacationsSuccessTest() {
        // Given
        User userA = User.createUser("", "",  "이서준", "", "19700723", "ADMIN", "9 ~ 6", "N");
        User userB = User.createUser("", "", "김서연", "", "19701026", "BP", "8 ~ 5", "N");
        User userC = User.createUser("", "", "김지후", "", "19740115", "BP", "10 ~ 7", "Y");

        LocalDateTime now = LocalDateTime.now();
        Vacation.createVacation(userA, VacationType.ANNUAL, new BigDecimal("4.0000"), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), "", "127.0.0.1");
        Vacation.createVacation(userA, VacationType.MATERNITY, new BigDecimal("10.0000"), LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0), LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59).plusMonths(6), "", "127.0.0.1");
        Vacation.createVacation(userA, VacationType.OVERTIME, new BigDecimal("0.3750"), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), "", "127.0.0.1");
        Vacation.createVacation(userB, VacationType.ANNUAL, new BigDecimal("4.0000"), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), "", "127.0.0.1");

        given(userRepositoryImpl.findUsersWithVacations()).willReturn(List.of(
                userA, userB, userC
        ));

        // When
        List<User> users = vacationService.getUserGroupVacations();

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
    @DisplayName("등록 가능 휴가 목록 조회 테스트 - 성공")
    void getAvailableVacationSuccessTest() {
        // Given
        String userId = "test1";
        User user = User.createUser("test1");
        LocalDateTime now = LocalDateTime.now();

        given(userService.checkUserExist(userId)).willReturn(user);
        given(vacationRepositoryImpl.findVacationsByBaseTime(userId, now)).willReturn(List.of(
                Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("4.0000"), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), "", "127.0.0.1"),
                Vacation.createVacation(user, VacationType.MATERNITY, new BigDecimal("10.0000"), LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0), LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59).plusMonths(6), "", "127.0.0.1"),
                Vacation.createVacation(user, VacationType.OVERTIME, new BigDecimal("0.3750"), LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0), LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59), "", "127.0.0.1")
        ));

        // When
        List<Vacation> vacations = vacationService.getAvailableVacations(userId, now);

        // Then
        then(userService).should().checkUserExist(userId);
        assertThat(vacations).hasSize(3);
        assertThat(vacations)
                .extracting("type")
                .containsExactlyInAnyOrder(VacationType.ANNUAL, VacationType.MATERNITY, VacationType.OVERTIME);
    }

    @Test
    @DisplayName("휴가 등록 내역 삭제 테스트 - 성공")
    void deleteRegistVacationHistorySuccessTest() {
        // Given
        Long historyId = 1L;
        Long vacationId = 1L;

        String desc = "1분기 정기 휴가";
        VacationType type = VacationType.ANNUAL;
        BigDecimal grantTime = new BigDecimal("4.0000");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime occurDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59);

        User user = User.createUser("test1");
        Vacation vacation = Vacation.createVacation(user, type, grantTime, occurDate, expiryDate, "", "127.0.0.1");
        VacationHistory history = VacationHistory.createRegistVacationHistory(vacation, desc, grantTime, "", "127.0.0.1");

        setVacationId(vacation, vacationId);
        given(vacationHistoryRepositoryImpl.findById(historyId)).willReturn(Optional.of(history));
        given(vacationRepositoryImpl.findById(vacationId)).willReturn(Optional.of(vacation));

        // When
        vacationService.deleteVacationHistory(historyId,"", "127.0.0.1");

        // Then
        then(vacationHistoryRepositoryImpl).should().findById(historyId);
        then(vacationRepositoryImpl).should().findById(vacationId);

        assertThat(history.getDelYN()).isEqualTo("Y");
        assertThat(vacation.getRemainTime()).isEqualTo(new BigDecimal("0.0000"));
    }

    @Test
    @DisplayName("휴가 사용 내역 삭제 테스트 - 성공")
    void deleteUseVacationHistorySuccessTest() {
        // Given
        Long historyId = 1L;
        Long vacationId = 1L;

        String desc = "연차";
        VacationTimeType timeType = VacationTimeType.DAYOFF;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime usedDateTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0).plusDays(1);

        User user = User.createUser("test1");
        Vacation vacation = Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("4.0000"),
                LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0),
                LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59),
                "", "127.0.0.1");
        VacationHistory history = VacationHistory.createUseVacationHistory(vacation, desc, timeType, usedDateTime, "", "127.0.0.1");

        setVacationId(vacation, vacationId);
        given(vacationHistoryRepositoryImpl.findById(historyId)).willReturn(Optional.of(history));
        given(vacationRepositoryImpl.findById(vacationId)).willReturn(Optional.of(vacation));

        // When
        vacationService.deleteVacationHistory(historyId, "", "127.0.0.1");

        // Then
        assertThat(history.getDelYN()).isEqualTo("Y");
        assertThat(vacation.getRemainTime()).isEqualTo(new BigDecimal("5.0000"));
    }

    @Test
    @DisplayName("휴가 삭제 테스트 - 실패 (내역 찾기 실패)")
    void deleteVacationHistoryFailTestNotFoundHistory() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.deleteVacationHistory(1L, "", "127.0.0.1"));
    }

    @Test
    @DisplayName("휴가 삭제 테스트 - 실패 (유효 기간 지남)")
    void deleteVacationHistoryFailTestPassedExpiryDate() {
        // Given
        Long historyId = 1L;
        Long vacationId = 1L;

        String desc = "연차";
        VacationTimeType timeType = VacationTimeType.DAYOFF;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime usedDateTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0).plusDays(1);

        User user = User.createUser("test1");
        Vacation vacation = Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("4.0000"),
                LocalDateTime.of(now.getYear()-1, 1, 1, 0, 0, 0),
                LocalDateTime.of(now.getYear()-1, 12, 31, 23, 59, 59),
                "", "127.0.0.1");
        VacationHistory history = VacationHistory.createUseVacationHistory(vacation, desc, timeType, usedDateTime, "", "127.0.0.1");

        setVacationId(vacation, vacationId);
        given(vacationHistoryRepositoryImpl.findById(historyId)).willReturn(Optional.of(history));
        given(vacationRepositoryImpl.findById(vacationId)).willReturn(Optional.of(vacation));

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.deleteVacationHistory(historyId, "", "127.0.0.1"));
    }

    @Test
    @DisplayName("휴가 삭제 테스트 - 실패 (잔여 휴가 부족)")
    void deleteRegistVacationHistoryFailTestNotEnoughRemainTime() {
        // Given
        Long historyId = 1L;
        Long vacationId = 1L;

        String desc = "1분기 정기 휴가";
        VacationType type = VacationType.ANNUAL;
        BigDecimal grantTime = new BigDecimal("4.0000");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime occurDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        LocalDateTime expiryDate = LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59);

        User user = User.createUser("test1");
        Vacation vacation = Vacation.createVacation(user, type, new BigDecimal("2.0000"), occurDate, expiryDate, "", "127.0.0.1");
        VacationHistory history = VacationHistory.createRegistVacationHistory(vacation, desc, grantTime, "", "127.0.0.1");

        setVacationId(vacation, vacationId);
        given(vacationHistoryRepositoryImpl.findById(historyId)).willReturn(Optional.of(history));
        given(vacationRepositoryImpl.findById(vacationId)).willReturn(Optional.of(vacation));

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.deleteVacationHistory(historyId, "", "127.0.0.1"));
    }

    @Test
    @DisplayName("휴가 삭제 테스트 - 실패 (사용 날짜 지남)")
    void deleteUseVacationHistoryFailTestPassedUsedDateTime() {
        // Given
        Long historyId = 1L;
        Long vacationId = 1L;

        String desc = "연차";
        VacationTimeType timeType = VacationTimeType.DAYOFF;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime usedDateTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0).minusDays(3);

        User user = User.createUser("test1");
        Vacation vacation = Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("4.0000"),
                LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0),
                LocalDateTime.of(now.getYear(), 12, 31, 23, 59, 59),
                "", "127.0.0.1");
        VacationHistory history = VacationHistory.createUseVacationHistory(vacation, desc, timeType, usedDateTime, "", "127.0.0.1");

        setVacationId(vacation, vacationId);
        given(vacationHistoryRepositoryImpl.findById(historyId)).willReturn(Optional.of(history));
        given(vacationRepositoryImpl.findById(vacationId)).willReturn(Optional.of(vacation));

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                vacationService.deleteVacationHistory(historyId, "", "127.0.0.1"));
    }

    @Test
    @DisplayName("기간별 내역 리스트 조회 테스트 - 성공")
    void getPeriodVacationUseHistoriesSuccessTest() {
        // Given
        User user = User.createUser("test1");
        Vacation vacation = Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("10.0000"),
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59),
                "", "127.0.0.1");

        VacationHistory history1 = VacationHistory.createUseVacationHistory(vacation, "연차", VacationTimeType.DAYOFF,
                LocalDateTime.of(2025, 2, 3, 0, 0, 0),
                "", "127.0.0.1");
        VacationHistory history2 = VacationHistory.createUseVacationHistory(vacation, "오전 반차", VacationTimeType.MORNINGOFF,
                LocalDateTime.of(2025, 2, 28, 9, 0, 0),
                "", "127.0.0.1");
        VacationHistory history3 = VacationHistory.createUseVacationHistory(vacation, "오후 반차", VacationTimeType.AFTERNOONOFF,
                LocalDateTime.of(2025, 2, 28, 14, 0, 0),
                "", "127.0.0.1");

        setVacationHistoryId(history1, 1L);
        setVacationHistoryId(history2, 2L);
        setVacationHistoryId(history3, 3L);
        List<VacationHistory> histories = List.of(history1, history2, history3);

        given(vacationHistoryRepositoryImpl.findVacationHistorysByPeriod(
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59)
        )).willReturn(histories);
        given(vacationRepositoryImpl.findVacationsByIdsWithUser(anyList()))
                .willReturn(List.of(vacation));

        // When
        List<VacationServiceDto> result = vacationService.getPeriodVacationUseHistories(
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59)
        );

        // Then
        then(vacationRepositoryImpl).should().findVacationsByIdsWithUser(anyList());
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("기간별 내역 리스트 조회 테스트 - 성공 (연차 그룹)")
    void getPeriodVacationUseHistoriesSuccessTestAnnualGroup() {
        // Given
        User user = User.createUser("test1");
        Vacation vacation1 = Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("10.0000"),
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 12, 31, 23, 59, 59),
                "", "127.0.0.1");
        VacationHistory history1 = VacationHistory.createUseVacationHistory(vacation1, "연차", VacationTimeType.DAYOFF,
                LocalDateTime.of(2024, 2, 5, 0, 0, 0),
                "", "127.0.0.1");
        VacationHistory history2 = VacationHistory.createUseVacationHistory(vacation1, "연차", VacationTimeType.DAYOFF,
                LocalDateTime.of(2024, 4, 11, 0, 0, 0),
                "", "127.0.0.1");
        VacationHistory history3 = VacationHistory.createUseVacationHistory(vacation1, "연차", VacationTimeType.DAYOFF,
                LocalDateTime.of(2024, 4, 12, 0, 0, 0),
                "", "127.0.0.1");

        Vacation vacation2 = Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("10.0000"),
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59),
                "", "127.0.0.1");
        VacationHistory history4 = VacationHistory.createUseVacationHistory(vacation2, "연차", VacationTimeType.DAYOFF,
                LocalDateTime.of(2025, 4, 10, 0, 0, 0),
                "", "127.0.0.1");
        VacationHistory history5 = VacationHistory.createUseVacationHistory(vacation2, "연차", VacationTimeType.DAYOFF,
                LocalDateTime.of(2025, 4, 11, 0, 0, 0),
                "", "127.0.0.1");
        VacationHistory history6 = VacationHistory.createUseVacationHistory(vacation2, "연차", VacationTimeType.DAYOFF,
                LocalDateTime.of(2025, 6, 2, 0, 0, 0),
                "", "127.0.0.1");

        setVacationId(vacation1, 1L);
        setVacationId(vacation2, 2L);
        setVacationHistoryId(history1, 1L);
        setVacationHistoryId(history2, 2L);
        setVacationHistoryId(history3, 3L);
        setVacationHistoryId(history4, 4L);
        setVacationHistoryId(history5, 5L);
        setVacationHistoryId(history6, 6L);
        List<VacationHistory> histories = List.of(history1, history2, history3, history4, history5, history6);

        given(vacationHistoryRepositoryImpl.findVacationHistorysByPeriod(
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59)
        )).willReturn(histories);
        given(vacationRepositoryImpl.findVacationsByIdsWithUser(anyList()))
                .willReturn(List.of(vacation1, vacation2));

        // When
        List<VacationServiceDto> result = vacationService.getPeriodVacationUseHistories(
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59)
        );

        // Then
        then(vacationRepositoryImpl).should().findVacationsByIdsWithUser(anyList());
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("기간별 내역 리스트 조회 테스트 - 성공 (연차 1건)")
    void getPeriodVacationUseHistoriesSuccessTestOneAnnual() {
        // Given
        User user = User.createUser("test1");
        Vacation vacation = Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("10.0000"),
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59),
                "", "127.0.0.1");

        VacationHistory history = VacationHistory.createUseVacationHistory(vacation, "연차", VacationTimeType.DAYOFF,
                LocalDateTime.of(2025, 2, 3, 0, 0, 0),
                "", "127.0.0.1");

        setVacationHistoryId(history, 1L);
        List<VacationHistory> histories = List.of(history);

        given(vacationHistoryRepositoryImpl.findVacationHistorysByPeriod(
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59)
        )).willReturn(histories);
        given(vacationRepositoryImpl.findVacationsByIdsWithUser(anyList()))
                .willReturn(List.of(vacation));

        // When
        List<VacationServiceDto> result = vacationService.getPeriodVacationUseHistories(
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59)
        );

        // Then
        then(vacationRepositoryImpl).should().findVacationsByIdsWithUser(anyList());
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("사용자 휴가 통계 조회 - 성공")
    void getUserVacationUseStatsSuccessTest() {
        // Given
        String userId = "testUser";
        LocalDateTime baseTime = LocalDateTime.of(2025, 8, 20, 10, 0, 0);
        LocalDateTime prevMonthTime = baseTime.minusMonths(1);

        User user = User.createUser(userId);
        Vacation vacation = Vacation.createVacation(user, VacationType.ANNUAL, new BigDecimal("13.0000"), // 현재 잔여 휴가 13일
                baseTime.minusYears(1), baseTime.plusYears(1), "admin", "127.0.0.1");

        // Histories
        VacationHistory grantHistory = VacationHistory.createRegistVacationHistory(vacation, "정기휴가", new BigDecimal("15.0000"), "admin", "127.0.0.1");
        setCreatedAt(grantHistory, baseTime.minusMonths(2)); // 2달 전 휴가 부여
        VacationHistory usedJuly = VacationHistory.createUseVacationHistory(vacation, "7월휴가", VacationTimeType.DAYOFF, baseTime.minusMonths(1).withDayOfMonth(15), "user", "127.0.0.1");
        VacationHistory usedAugust = VacationHistory.createUseVacationHistory(vacation, "8월휴가", VacationTimeType.DAYOFF, baseTime.withDayOfMonth(10), "user", "127.0.0.1");
        VacationHistory scheduledSept = VacationHistory.createUseVacationHistory(vacation, "9월휴가", VacationTimeType.DAYOFF, baseTime.plusMonths(1).withDayOfMonth(5), "user", "127.0.0.1");
        vacation.getHistorys().addAll(List.of(grantHistory, usedJuly, usedAugust, scheduledSept));

        // Mocking
        // findVacationsByBaseTimeWithHistory는 baseTime 기준으로 유효한 휴가와 그에 따른 전체 이력을 반환한다고 가정
        given(vacationRepositoryImpl.findVacationsByBaseTimeWithHistory(userId, baseTime)).willReturn(List.of(vacation));

        // When
        VacationStatsDto result = vacationService.getUserVacationUseStats(userId, baseTime);

        // Then
        // --- 검증 --- 
        // As of Aug 20 (current)
        // granted: 15 (2달 전 부여)
        // used: 2 (July, Aug)
        // remain: 15 - 2 = 13
        // scheduled: 1 (Sept)
        assertThat(result.getRemainTime()).isEqualByComparingTo("13.0000");
        assertThat(result.getUsedTime()).isEqualByComparingTo("2.0000");
        assertThat(result.getScheduledTime()).isEqualByComparingTo("1.0000");

        // As of July 20 (previous)
        // granted: 15
        // used: 1 (July)
        // remain: 15 - 1 = 14
        // scheduled: 2 (Aug, Sept)
        assertThat(result.getPreviousRemainTime()).isEqualByComparingTo("14.0000");
        assertThat(result.getPreviousUsedTime()).isEqualByComparingTo("1.0000");
        assertThat(result.getPreviousScheduledTime()).isEqualByComparingTo("2.0000");

        // Compare
        // remainCompare: 13 - 14 = -1
        // usedCompare: 2 - 1 = 1
        // scheduledCompare: 1 - 2 = -1
        assertThat(result.getRemainTimeCompare()).isEqualByComparingTo("-1.0000");
        assertThat(result.getUsedTimeCompare()).isEqualByComparingTo("1.0000");
        assertThat(result.getScheduledTimeCompare()).isEqualByComparingTo("-1.0000");

        then(vacationRepositoryImpl).should().findVacationsByBaseTimeWithHistory(userId, baseTime);
    }

    // 테스트 헬퍼 메서드
    private void setVacationId(Vacation vacation, Long id) {
        try {
            java.lang.reflect.Field field = Vacation.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(vacation, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setVacationHistoryId(VacationHistory history, Long id) {
        try {
            java.lang.reflect.Field field = VacationHistory.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(history, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCreatedAt(AuditingFields entity, LocalDateTime dateTime) {
        try {
            java.lang.reflect.Field field = AuditingFields.class.getDeclaredField("createdAt");
            field.setAccessible(true);
            field.set(entity, dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
