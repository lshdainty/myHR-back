package com.lshdainty.myhr.api;

import com.lshdainty.myhr.domain.Vacation;
import com.lshdainty.myhr.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VacationApiController {
    private final VacationRepository vacationRepository;

    @GetMapping("/test")
    public List<Vacation> getAllVacations() {
        log.info("test {}", vacationRepository.findVacationsByUserNo(1L).toString());

        log.info("test2 {}", vacationRepository.findVacationsByYear("2025").toString());
        return null;
    }

}
