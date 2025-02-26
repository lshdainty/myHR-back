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
    public Long save(String name, String date) {
        Holiday holiday = Holiday.addHoliday(name, date);
        holidayRepository.save(holiday);
        return holiday.getSeq();
    }

    public Holiday findHoliday(Long seq) {
        return holidayRepository.findHoliday(seq);
    }

    public List<Holiday> findHolidays() {
        return holidayRepository.findHolidays();
    }

    @Transactional
    public void editHoliday(Long holidaySeq, String name, String date) {
        Holiday findHoliday = holidayRepository.findHoliday(holidaySeq);
        findHoliday.updateHoliday(name, date);
    }

    @Transactional
    public void deleteHoliday(Long holidaySeq) {
        Holiday findHoliday = holidayRepository.findHoliday(holidaySeq);
        holidayRepository.delete(findHoliday);
    }

    // 권장휴무는 단순 조회만 함
    public List<Recommend> findRecommends() {
        return reommendRepository.findRecommends();
    }
}
