package com.lshdainty.myhr.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lshdainty.myhr.domain.Holiday;
import com.lshdainty.myhr.domain.Recommend;
import com.lshdainty.myhr.service.HolidayService;
import lombok.Data;
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
    public ApiResponse registHoliday(@RequestBody HolidayReq req) {
        String holidayName = req.getName();
        String holidayDate = req.getDate();

        Long holidaySeq = holidayService.save(holidayName, holidayDate);

        HolidayResp resp = new HolidayResp();
        resp.setSeq(holidaySeq);

        return ApiResponse.success(resp);
    }

    @GetMapping("api/v1/holidays")
    public ApiResponse getPeriodHolidays(@RequestParam("daytype") String daytype) {
        List<HolidayResp> resps = null;

        if(daytype.equals("holiday")) {
            List<Holiday> holidays = holidayService.findHolidays();
            resps = holidays.stream()
                    .map(h -> new HolidayResp(h))
                    .collect(Collectors.toList());
        } else if (daytype.equals("recommend")) {
            List<Recommend> recommends = holidayService.findRecommends();
            resps = recommends.stream()
                    .map(r -> new HolidayResp(r))
                    .collect(Collectors.toList());
        }

        return ApiResponse.success(resps);
    }

    @PutMapping("/api/v1/holiday/{seq}")
    public ApiResponse editHoliday(@PathVariable("seq") Long seq, @RequestBody HolidayReq req) {
        String holidayName = req.getName();
        String holidayDate = req.getDate();
        holidayService.editHoliday(seq, holidayName, holidayDate);

        Holiday findHoliday = holidayService.findHoliday(seq);
        HolidayResp resp = new HolidayResp(findHoliday);

        return ApiResponse.success(resp);
    }

    @DeleteMapping("/api/v1/holiday/{seq}")
    public ApiResponse deleteHoliday(@PathVariable("seq") Long seq) {
        holidayService.deleteHoliday(seq);
        return ApiResponse.success();
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class HolidayReq {
        private Long seq;
        private String name;
        private String date;
        private String type;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class HolidayResp {
        private Long seq;
        private String name;
        private String date;
        private String type;

        public HolidayResp() {}

        public HolidayResp(Holiday holiday) {
            this.seq = holiday.getSeq();
            this.name = holiday.getName();
            this.date = holiday.getDate();
            this.type = "holiday";
        }

        public HolidayResp(Recommend recommend) {
            this.seq = recommend.getSeq();
            this.name = recommend.getName();
            this.date = recommend.getDate();
            this.type = "recommend";
        }
    }
}
