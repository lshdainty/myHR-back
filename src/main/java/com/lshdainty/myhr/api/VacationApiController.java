package com.lshdainty.myhr.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.domain.Vacation;
import com.lshdainty.myhr.domain.VacationType;
import com.lshdainty.myhr.dto.UserDto;
import com.lshdainty.myhr.dto.VacationDto;
import com.lshdainty.myhr.service.VacationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VacationApiController {
    private final VacationService vacationService;

    @PostMapping("/api/v1/vacation")
    public ApiResponse addVacation(@RequestBody VacationDto vacationDto, HttpServletRequest req) {
        Long vacationId = vacationService.addVacation(
                vacationDto.getUserNo(),
                vacationDto.getVacationName(),
                vacationDto.getVacationDesc(),
                vacationDto.getVacationType(),
                vacationDto.getGrantTime(),
                vacationDto.getOccurDate(),
                vacationDto.getExpiryDate(),
                0L, // 추후 로그인한 유저의 id를 가져와서 여기에다 넣을 것
                req.getRemoteAddr()
        );

        return ApiResponse.success(new VacationDto(vacationId));
    }

    @GetMapping("/api/v1/vacations/user/{userNo}")
    public ApiResponse getVacationsByUser(@PathVariable("userNo") Long userNo) {
        List<Vacation> vacations = vacationService.findVacationsByUser(userNo);

        List<VacationDto> resp = vacations.stream()
                .map(v -> new VacationDto(v))
                .collect(Collectors.toList());

        return ApiResponse.success(resp);
    }

    @GetMapping("/api/v1/vacations/usergroup")
    public ApiResponse getVacationsByUserGroup() {
        List<User> usersVacations = vacationService.findVacationsByUserGroup();

        List<UserDto> resp = new ArrayList<>();
        for (User user : usersVacations) {
            List<VacationDto> vacations = user.getVacations().stream()
                    .map(VacationDto::new)
                    .toList();

            resp.add(new UserDto(user, vacations));
        }

        return ApiResponse.success(resp);
    }

    @PutMapping("/api/v1/vacation/{id}")
    public ApiResponse editVacation(@PathVariable("id") Long vacationId, @RequestBody VacationDto vacationDto, HttpServletRequest req) {
        Vacation vacation = vacationService.editVacation(
                vacationId,
                vacationDto.getUserNo(),
                vacationDto.getVacationName(),
                vacationDto.getVacationDesc(),
                vacationDto.getVacationType(),
                vacationDto.getGrantTime(),
                vacationDto.getOccurDate(),
                vacationDto.getExpiryDate(),
                0L, // 추후 로그인한 유저의 id를 가져와서 여기에다 넣을 것
                req.getRemoteAddr()
        );

        return ApiResponse.success(new VacationDto(vacation));
    }

    @DeleteMapping("/api/v1/vacation/{id}")
    public ApiResponse deleteVacation(@PathVariable("id") Long vacationId, HttpServletRequest req) {
        Long delUserNo = 0L;   // 추후 로그인 한 사람의 id를 가져와서 삭제한 사람의 userNo에 세팅
        vacationService.deleteVacation(vacationId, delUserNo, req.getRemoteAddr());
        return ApiResponse.success();
    }
}
