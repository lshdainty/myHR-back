package com.lshdainty.myhr.api;

import com.lshdainty.myhr.domain.Holiday;
import com.lshdainty.myhr.domain.Recommend;
import com.lshdainty.myhr.dto.HolidayDto;
import com.lshdainty.myhr.service.HolidayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HolidayApiController {
    private final HolidayService holidayService;

    @PostMapping("api/v1/holiday")
    public ApiResponse registHoliday(@RequestBody HolidayDto req) {
        Long holidaySeq = holidayService.save(req.getHolidayName(), req.getHolidayDate());
        return ApiResponse.success(new HolidayDto(holidaySeq));
    }

    @GetMapping("api/v1/holidays")
    public ApiResponse getPeriodHolidays(@RequestParam("daytype") String daytype) {
        List<HolidayDto> resps = null;

        if(daytype.equals("holiday")) {
            List<Holiday> holidays = holidayService.findHolidays();
            resps = holidays.stream()
                    .map(h -> new HolidayDto(h))
                    .collect(Collectors.toList());
        } else if (daytype.equals("recommend")) {
            List<Recommend> recommends = holidayService.findRecommends();
            resps = recommends.stream()
                    .map(r -> new HolidayDto(r))
                    .collect(Collectors.toList());
        }

        return ApiResponse.success(resps);
    }

    @PutMapping("/api/v1/holiday/{seq}")
    public ApiResponse editHoliday(@PathVariable("seq") Long seq, @RequestBody HolidayDto req) {
        holidayService.editHoliday(seq, req.getHolidayName(), req.getHolidayDate());

        Holiday findHoliday = holidayService.findHoliday(seq);
        return ApiResponse.success(new HolidayDto(findHoliday));
    }

    @DeleteMapping("/api/v1/holiday/{seq}")
    public ApiResponse deleteHoliday(@PathVariable("seq") Long seq) {
        holidayService.deleteHoliday(seq);
        return ApiResponse.success();
    }
}
