package com.lshdainty.myhr.api;

import com.lshdainty.myhr.domain.Dues;
import com.lshdainty.myhr.dto.DuesDto;
import com.lshdainty.myhr.service.DuesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DuesApiController {
    private final DuesService duesService;

    @PostMapping("/api/v1/dues")
    public ApiResponse registDues(@RequestBody DuesDto duesDto) {
        Long duesSeq = duesService.save(
                duesDto.getDuesUserName(),
                duesDto.getDuesAmount(),
                duesDto.getDuesType(),
                duesDto.getDuesDate(),
                duesDto.getDuesDetail()
        );
        return ApiResponse.success(new DuesDto(duesSeq));
    }

    @GetMapping("/api/v1/dues")
    public ApiResponse getYearDues(@RequestParam("year") String year) {
        List<Dues> dues = duesService.findDuesByYear(year);

        List<DuesDto> resp = dues.stream()
                .map(d -> new DuesDto(d))
                .collect(Collectors.toList());

        int total = 0;
        for (DuesDto duesDto : resp) {
            total = duesDto.getDuesType().applyAsType(total, duesDto.getDuesAmount());
            duesDto.setDuesTotal(total);
        }

        return ApiResponse.success(resp);
    }

    @DeleteMapping("/api/v1/dues/{seq}")
    public ApiResponse deleteHoliday(@PathVariable("seq") Long seq) {
        duesService.deleteDues(seq);
        return ApiResponse.success();
    }
}