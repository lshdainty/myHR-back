package com.lshdainty.myhr.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lshdainty.myhr.domain.Schedule;
import com.lshdainty.myhr.domain.ScheduleType;
import com.lshdainty.myhr.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ScheduleApiController {
    private final ScheduleService scheduleService;

    @PostMapping("/api/v1/schedule")
    public ApiResponse addSchedule(@RequestBody ScheduleReq scheduleReq, HttpServletRequest req) {
        Long scheduleId = null;

        if (scheduleReq.getScheduleType().isVacationType()) {
            scheduleId = scheduleService.addSchedule(
                    scheduleReq.getUserNo(),
                    scheduleReq.getVacationId(),
                    scheduleReq.getScheduleType(),
                    scheduleReq.getScheduleDesc(),
                    scheduleReq.getStartDate(),
                    scheduleReq.getEndDate(),
                    0L, // 추후 로그인한 유저의 id를 가져와서 여기에다 넣을 것
                    req.getRemoteAddr()
            );
        } else {
            scheduleId = scheduleService.addSchedule(
                    scheduleReq.getUserNo(),
                    scheduleReq.getScheduleType(),
                    scheduleReq.getScheduleDesc(),
                    scheduleReq.getStartDate(),
                    scheduleReq.getEndDate(),
                    0L, // 추후 로그인한 유저의 id를 가져와서 여기에다 넣을 것
                    req.getRemoteAddr()
            );
        }

        return ApiResponse.success(new ScheduleResp(scheduleId));
    }

    @GetMapping("/api/v1/schedules/user/{userNo}")
    public ApiResponse getSchedulesByUser(@PathVariable("userNo") Long userNo) {
        List<Schedule> schedules = scheduleService.findSchedulesByUserNo(userNo);

        List<ScheduleResp> resp = schedules.stream()
                .map(s -> new ScheduleResp(s))
                .collect(Collectors.toList());

        return ApiResponse.success(resp);
    }

    @DeleteMapping("/api/v1/schedule/{id}")
    public ApiResponse deleteSchedule(@PathVariable("id") Long scheduleId, HttpServletRequest req) {
        Long delUserNo = 0L;   // 추후 로그인 한 사람의 id를 가져와서 삭제한 사람의 userNo에 세팅
        scheduleService.deleteSchedule(scheduleId, delUserNo, req.getRemoteAddr());
        return ApiResponse.success();
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class ScheduleReq {
        private Long userNo;
        private Long vacationId;
        private ScheduleType scheduleType;
        private String scheduleDesc;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class ScheduleResp {
        private Long scheduleId;
        private Long userNo;
        private Long vacationId;
        private ScheduleType scheduleType;
        private String scheduleDesc;
        private LocalDateTime startDate;
        private LocalDateTime endDate;

        public ScheduleResp(Long id) { scheduleId = id; }

        public ScheduleResp(Schedule schedule) {
            scheduleId = schedule.getId();
            userNo = schedule.getUser().getId();
            if (schedule.getType().isVacationType()) {
                vacationId = schedule.getVacation().getId();
            }
            scheduleType = schedule.getType();
            scheduleDesc = schedule.getDesc();
            startDate = schedule.getStartDate();
            endDate = schedule.getEndDate();
        }
    }
}
