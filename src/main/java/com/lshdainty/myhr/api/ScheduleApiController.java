package com.lshdainty.myhr.api;

import com.lshdainty.myhr.domain.ScheduleType;
import com.lshdainty.myhr.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ScheduleApiController {
    private final ScheduleService scheduleService;

    @GetMapping("/api/v1/test")
    public void test() {
        scheduleService.addSchedule(
                1L,
                "",
                1L,
                ScheduleType.DAYOFF,
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L,
                "");
    }
}
