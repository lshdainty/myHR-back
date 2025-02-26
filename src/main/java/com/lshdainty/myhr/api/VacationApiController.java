package com.lshdainty.myhr.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.domain.Vacation;
import com.lshdainty.myhr.domain.VacationType;
import com.lshdainty.myhr.service.VacationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse addVacation(@RequestBody VacationReq vacationReq, HttpServletRequest req) {
        Long vacationId = vacationService.addVacation(
                vacationReq.getUserNo(),
                vacationReq.getVacationName(),
                vacationReq.getVacationDesc(),
                vacationReq.getVacationType(),
                vacationReq.getGrantTime(),
                vacationReq.getExpiryDate(),
                0L, // 추후 로그인한 유저의 id를 가져와서 여기에다 넣을 것
                req.getRemoteAddr()
        );

        return ApiResponse.success(new VacationResp(vacationId));
    }

    @GetMapping("/api/v1/vacations/user/{userNo}")
    public ApiResponse getVacationsByUser(@PathVariable("userNo") Long userNo) {
        List<Vacation> vacations = vacationService.findVacationsByUser(userNo);

        List<VacationResp> resp = vacations.stream()
                .map(v -> new VacationResp(v))
                .collect(Collectors.toList());

        return ApiResponse.success(resp);
    }

    @GetMapping("/api/v1/vacations/usergroup")
    public ApiResponse getVacationsByUserGroup() {
        List<User> usersVacations = vacationService.findVacationsByUserGroup();

        List<UserVacationsResp> resp = usersVacations.stream()
                .map(u -> new UserVacationsResp(u))
                .collect(Collectors.toList());

        return ApiResponse.success(resp);
    }

    @DeleteMapping("/api/v1/vacation/{id}")
    public ApiResponse deleteVacation(@PathVariable("id") Long vacationId, HttpServletRequest req) {
        Long delUserNo = 1L;   // 추후 로그인 한 사람의 id를 가져와서 삭제한 사람의 userNo에 세팅
        vacationService.deleteVacation(vacationId, delUserNo, req.getRemoteAddr());
        return ApiResponse.success();
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class VacationReq {
        private Long userNo;
        private String vacationName;
        private String vacationDesc;
        private VacationType vacationType;
        private float grantTime;
        private LocalDateTime expiryDate;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class UserVacationsResp {
        private Long userNo;
        private String userName;
        private float standardTime = 0.0f; // 기본 휴가
        private float addedTime = 0.0f;    // 추가 휴가
        private List<VacationResp> vacations = new ArrayList<>();

        public UserVacationsResp(User user) {
            userNo = user.getId();
            userName = user.getName();
            vacations = user.getVacations().stream().map(v -> new VacationResp(v)).collect(Collectors.toList());

            vacations.forEach(v -> {
                if (v.getVacationType().equals(VacationType.BASIC)) {
                    standardTime += v.getGrantTime();
                } else if (v.getVacationType().equals(VacationType.ADDED)) {
                    addedTime += v.getGrantTime();
                }
            });
        }
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class VacationResp {
        private Long vacationId;
        private String vacationName;
        private String vacationDesc;
        private VacationType vacationType;
        private float grantTime;
        private LocalDateTime expiryDate;

        public VacationResp(Long id) {
            vacationId = id;
        }

        public VacationResp(Vacation vacation) {
            vacationName = vacation.getName();
            vacationId = vacation.getId();
            vacationDesc = vacation.getDesc();
            vacationType = vacation.getType();
            grantTime = vacation.getGrantTime();
            expiryDate = vacation.getExpiryDate();
        }
    }
}
