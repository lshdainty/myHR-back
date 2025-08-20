package com.lshdainty.myhr.api;

import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.domain.Vacation;
import com.lshdainty.myhr.domain.VacationTimeType;
import com.lshdainty.myhr.dto.UserDto;
import com.lshdainty.myhr.dto.VacationDto;
import com.lshdainty.myhr.service.VacationService;
import com.lshdainty.myhr.service.dto.VacationServiceDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VacationApiController {
    private final VacationService vacationService;

    @PostMapping("/api/v1/vacation")
    public ApiResponse registVacation(@RequestBody VacationDto data, HttpServletRequest req) {
        Long vacationId = vacationService.registVacation(VacationServiceDto.builder()
                        .userId(data.getUserId())
                        .desc(data.getVacationDesc())
                        .type(data.getVacationType())
                        .grantTime(data.getGrantTime())
                        .occurDate(data.getOccurDate())
                        .expiryDate(data.getExpiryDate())
                        .build(),
                "", // 추후 로그인한 유저의 id를 가져와서 여기에다 넣을 것
                req.getRemoteAddr()
        );

        return ApiResponse.success(VacationDto.builder().vacationId(vacationId).build());
    }

    @PostMapping("/api/v1/vacation/use/{vacationId}")
    public ApiResponse useVacation(@PathVariable("vacationId") Long vacationId, @RequestBody VacationDto data, HttpServletRequest req) {
        Long respVacationId = vacationService.useVacation(VacationServiceDto.builder()
                        .userId(data.getUserId())
                        .id(vacationId)
                        .desc(data.getVacationDesc())
                        .timeType(data.getVacationTimeType())
                        .startDate(data.getStartDate())
                        .endDate(data.getEndDate())
                        .build(),
                "", // 추후 로그인한 유저의 id를 가져와서 여기에다 넣을 것
                req.getRemoteAddr()
        );

        return ApiResponse.success(VacationDto.builder().vacationId(respVacationId).build());
    }

    @GetMapping("/api/v1/vacations/user/{userId}")
    public ApiResponse getUserVacations(@PathVariable("userId") String userId) {
        List<Vacation> vacations = vacationService.getUserVacations(userId);

        List<VacationDto> resp = vacations.stream()
                .map(v -> VacationDto.builder()
                        .vacationId(v.getId())
                        .vacationType(v.getType())
                        .vacationTypeName(v.getType().getStrName())
                        .remainTime(v.getRemainTime())
                        .occurDate(v.getOccurDate())
                        .expiryDate(v.getExpiryDate())
                        .build()
                )
                .toList();

        return ApiResponse.success(resp);
    }

    @GetMapping("/api/v1/vacations/usergroup")
    public ApiResponse getUserGroupVacations() {
        List<User> usersVacations = vacationService.getUserGroupVacations();

        List<UserDto> resp = new ArrayList<>();
        for (User user : usersVacations) {
            List<VacationDto> vacations = user.getVacations().stream()
                    .map(v -> VacationDto.builder()
                            .vacationId(v.getId())
                            .vacationType(v.getType())
                            .vacationTypeName(v.getType().getStrName())
                            .remainTime(v.getRemainTime())
                            .occurDate(v.getOccurDate())
                            .expiryDate(v.getExpiryDate())
                            .build()
                    )
                    .toList();

            resp.add(UserDto.builder()
                    .userId(user.getId())
                    .userName(user.getName())
                    .vacations(vacations)
                    .build()
            );
        }

        return ApiResponse.success(resp);
    }

    @GetMapping("/api/v1/vacation/available/{userId}")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public ApiResponse getAvailableVacations(@PathVariable("userId") String userId, @RequestParam("startDate") LocalDateTime startDate) {
        List<Vacation> vacations = vacationService.getAvailableVacations(userId, startDate);

        List<VacationDto> resp = vacations.stream()
                .map(v -> VacationDto.builder()
                        .vacationId(v.getId())
                        .vacationType(v.getType())
                        .vacationTypeName(v.getType().getStrName())
                        .remainTime(v.getRemainTime())
                        .occurDate(v.getOccurDate())
                        .expiryDate(v.getExpiryDate())
                        .remainTimeStr(VacationTimeType.convertValueToDay(v.getRemainTime()))
                        .build()
                )
                .toList();

        return ApiResponse.success(resp);
    }

    @DeleteMapping("/api/v1/vacation/history/{id}")
    public ApiResponse deleteVacationHistory(@PathVariable("id") Long vacationHistoryId, HttpServletRequest req) {
        String delUserId = "";   // 추후 로그인 한 사람의 id를 가져와서 삭제한 사람의 userNo에 세팅
        vacationService.deleteVacationHistory(vacationHistoryId, delUserId, req.getRemoteAddr());
        return ApiResponse.success();
    }

    @GetMapping("/api/v1/vacation/use/histories/period")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public ApiResponse getPeriodVacationUseHistories(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        List<VacationServiceDto> histories = vacationService.getPeriodVacationUseHistories(startDate, endDate);

        List<VacationDto> resp = histories.stream()
                .map(v -> VacationDto.builder()
                        .userId(v.getUser().getId())
                        .userName(v.getUser().getName())
                        .vacationId(v.getId())
                        .vacationDesc(v.getDesc())
                        .vacationHistoryIds(v.getHistoryIds())
                        .vacationTimeType(v.getTimeType())
                        .vacationTimeTypeName(v.getTimeType().getStrName())
                        .startDate(v.getStartDate())
                        .endDate(v.getEndDate())
                        .build()
                )
                .toList();

        return ApiResponse.success(resp);
    }

    @GetMapping("/api/v1/vacation/use/histories/user/period")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public ApiResponse getUserPeriodVacationUseHistories(
            @RequestParam("userId") String userId,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        List<VacationServiceDto> histories = vacationService.getUserPeriodVacationUseHistories(userId, startDate, endDate);

        List<VacationDto> resp = histories.stream()
                .map(v -> VacationDto.builder()
                        .vacationId(v.getId())
                        .vacationDesc(v.getDesc())
                        .vacationHistoryId(v.getHistoryId())
                        .vacationTimeType(v.getTimeType())
                        .vacationTimeTypeName(v.getTimeType().getStrName())
                        .startDate(v.getStartDate())
                        .endDate(v.getEndDate())
                        .build()
                )
                .toList();

        return ApiResponse.success(resp);
    }

    @GetMapping("/api/v1/vacation/use/histories/user/month/stats")
    public ApiResponse getUserMonthStatsVacationUseHistories(
            @RequestParam("userId") String userId,
            @RequestParam("year") String year) {
        List<VacationServiceDto> histories = vacationService.getUserMonthStatsVacationUseHistories(userId, year);

        List<VacationDto> resp = histories.stream()
                .map(v -> VacationDto.builder()
                        .month(v.getMonth())
                        .usedDateTime(v.getUsedTime())
                        .usedDateTimeStr(VacationTimeType.convertValueToDay(v.getUsedTime()))
                        .build()
                )
                .toList();

        return ApiResponse.success(resp);
    }
}
