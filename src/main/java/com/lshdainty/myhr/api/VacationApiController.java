package com.lshdainty.myhr.api;

import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.domain.Vacation;
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
    public ApiResponse registVacation(@RequestBody VacationDto vacationDto, HttpServletRequest req) {
        Long vacationId = vacationService.registVacation(
                vacationDto.getUserId(),
                vacationDto.getVacationDesc(),
                vacationDto.getVacationType(),
                vacationDto.getGrantTime(),
                vacationDto.getOccurDate(),
                vacationDto.getExpiryDate(),
                "", // 추후 로그인한 유저의 id를 가져와서 여기에다 넣을 것
                req.getRemoteAddr()
        );

        return ApiResponse.success(VacationDto.builder().vacationId(vacationId).build());
    }

    @PostMapping("/api/v1/vacation/use/{vacationId}")
    public ApiResponse useVacation(@PathVariable("vacationId") Long vacationId, @RequestBody VacationDto vacationDto, HttpServletRequest req) {
        Long respVacationId = vacationService.useVacation(
                vacationDto.getUserId(),
                vacationId,
                vacationDto.getVacationDesc(),
                vacationDto.getVacationTimeType(),
                vacationDto.getStartDate(),
                vacationDto.getEndDate(),
                "", // 추후 로그인한 유저의 id를 가져와서 여기에다 넣을 것
                req.getRemoteAddr()
        );

        return ApiResponse.success(VacationDto.builder().vacationId(respVacationId).build());
    }

    @GetMapping("/api/v1/vacations/user/{userId}")
    public ApiResponse getVacationsByUser(@PathVariable("userId") String userId) {
        List<Vacation> vacations = vacationService.findVacationsByUser(userId);

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
    public ApiResponse getVacationsByUserGroup() {
        List<User> usersVacations = vacationService.findVacationsByUserGroup();

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

    @GetMapping("/api/v1/vacation/histories/period")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public ApiResponse getVacationHistoriesByPeriod(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        List<VacationServiceDto> histories = vacationService.getVacationHistoriesByPeriod(startDate, endDate);

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
}
