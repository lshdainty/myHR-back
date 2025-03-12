package com.lshdainty.myhr.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lshdainty.myhr.domain.Dues;
import com.lshdainty.myhr.domain.VacationType;
import com.lshdainty.myhr.service.DuesService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DuesApiController {
    private final DuesService duesService;

    @PostMapping("api/v1/dues")
    public ApiResponse registDues() {
        return ApiResponse.success();
    }

    @GetMapping("api/v1/dues")
    public ApiResponse getYearDues(@RequestParam("year") String year) {
        List<Dues> dues = duesService.findDuesByYear(year);

        List<DuesResp> resp = dues.stream()
                .map(d -> new DuesResp(d))
                .collect(Collectors.toList());

        return ApiResponse.success(resp);
    }

    @DeleteMapping("/api/v1/dues/{seq}")
    public ApiResponse deleteHoliday(@PathVariable("seq") Long seq) {
        duesService.deleteDues(seq);
        return ApiResponse.success();
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class DuesResp {
        private Long duesSeq;
        private String duesUserName;
        private int duesAmount;
        private String duesType;
        private String duesDate;
        private String duesDetail;
    }
}