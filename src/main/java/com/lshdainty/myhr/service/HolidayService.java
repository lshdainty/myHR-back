package com.lshdainty.myhr.service;

import com.lshdainty.myhr.domain.Holiday;
import com.lshdainty.myhr.domain.Recommend;
import com.lshdainty.myhr.repository.HolidayRepository;
import com.lshdainty.myhr.repository.ReommendRepository;
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
    private final ReommendRepository reommendRepository;

    @Transactional
    public Long save(Holiday holiday) {
        holidayRepository.save(holiday);
        return holiday.getSeq();
    }

    public List<Holiday> findHolidays() {
        return holidayRepository.findHolidays();
    }

    @Transactional
    public void editHoliday(Long holidaySeq, Holiday holiday) {
        Holiday findHoliday = holidayRepository.findHoliday(holidaySeq);

        if (!Objects.isNull(holiday.getName())) { findHoliday.setName(holiday.getName()); }
        if (!Objects.isNull(holiday.getDate())) { findHoliday.setDate(holiday.getDate()); }
    }

    @Transactional
    public void deleteHoliday(Long holidaySeq) {
        Holiday findHoliday = holidayRepository.findHoliday(holidaySeq);
        holidayRepository.delete(findHoliday);
    }

    public List<Recommend> findRecommends() {
        return reommendRepository.findRecommends();
    }
}
