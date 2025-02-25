package com.lshdainty.myhr.api;

import com.lshdainty.myhr.domain.User;
import com.lshdainty.myhr.domain.Vacation;
import com.lshdainty.myhr.domain.VacationType;
import com.lshdainty.myhr.service.VacationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VacationApiController {
    private final VacationService vacationService;

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
        Long deleteUserNo = 1L;   // 추후 로그인 한 사람의 id를 가져와서 삭제한 사람의 userNo에 세팅
        vacationService.deleteVacation(vacationId, deleteUserNo, req.getRemoteAddr());
        return ApiResponse.success();
    }

    @Data
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
                    standardTime += v.getGrantedTime();
                } else if (v.getVacationType().equals(VacationType.ADDED)) {
                    addedTime += v.getGrantedTime();
                }
            });
        }
    }

    @Data
    static class VacationResp {
        private Long vacationId;
        private String vacationName;
        private String vacationDescription;
        private VacationType vacationType;
        private float grantedTime = 0.0f;
        private LocalDateTime expirationDate;

        public VacationResp(Vacation vacation) {
            vacationName = vacation.getName();
            vacationId = vacation.getId();
            vacationDescription = vacation.getDescription();
            vacationType = vacation.getType();
            grantedTime = vacation.getGrantedTime();
            expirationDate = vacation.getExpirationDate();
        }
    }
}
