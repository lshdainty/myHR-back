package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.Holiday;
import com.lshdainty.myhr.domain.HolidayType;
import com.lshdainty.myhr.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HolidayService {
    private final HolidayRepository holidayRepository;

    @Transactional
    public Long save(String name, String date, HolidayType type) {
        Holiday holiday = Holiday.createHoliday(name, date, type);
        holidayRepository.save(holiday);
        return holiday.getSeq();
    }

    public Holiday findHoliday(Long seq) {
        Holiday holiday = holidayRepository.findHoliday(seq);

        // holiday 없으면 에러 반환
        if (Objects.isNull(holiday)) { throw new IllegalArgumentException("holiday not found"); }

        return holiday;
    }

    public List<Holiday> findHolidays() {
        return holidayRepository.findHolidays();
    }

    public List<Holiday> findHolidaysByType(HolidayType type) {
        return holidayRepository.findHolidaysByType(type);
    }

    @Transactional
    public void editHoliday(Long holidaySeq, String name, String date, HolidayType type) {
        Holiday findHoliday = holidayRepository.findHoliday(holidaySeq);

        // holiday 없으면 에러 반환
        if (Objects.isNull(findHoliday)) { throw new IllegalArgumentException("holiday not found"); }

        findHoliday.updateHoliday(name, date, type);
    }

    @Transactional
    public void deleteHoliday(Long holidaySeq) {
        Holiday findHoliday = holidayRepository.findHoliday(holidaySeq);

        // holiday 없으면 에러 반환
        if (Objects.isNull(findHoliday)) { throw new IllegalArgumentException("holiday not found"); }

        holidayRepository.delete(findHoliday);
    }
}
