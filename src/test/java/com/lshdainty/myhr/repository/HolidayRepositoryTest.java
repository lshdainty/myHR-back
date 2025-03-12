package com.lshdainty.myhr.repository;

import com.lshdainty.myhr.domain.Holiday;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(HolidayRepository.class)
@Transactional
@DisplayName("JPA 공휴일 레포지토리 테스트")
class HolidayRepositoryTest {
    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    @DisplayName("휴일 저장")
    void save() {
        // given
        String name = "신정";
        String date = "20250101";

        Holiday holiday = Holiday.createHoliday(name, date);

        // when
        holidayRepository.save(holiday);

        Holiday findHoliday = holidayRepository.findHoliday(holiday.getSeq());

        // then
        assertThat(findHoliday).isEqualTo(holiday);
        assertThat(findHoliday.getName()).isEqualTo(name);
        assertThat(findHoliday.getDate()).isEqualTo(date);
    }

    @Test
    @DisplayName("휴일 조회")
    void getHoliday() {
        // given
        String name = "신정";
        String date = "20250101";

        Holiday holiday = Holiday.createHoliday(name, date);
        holidayRepository.save(holiday);

        // when
        Holiday findHoliday = holidayRepository.findHoliday(holiday.getSeq());

        // then
        assertThat(findHoliday.getSeq()).isEqualTo(holiday.getSeq());
        assertThat(findHoliday.getName()).isEqualTo(holiday.getName());
        assertThat(findHoliday.getDate()).isEqualTo(holiday.getDate());
    }

    @Test
    @DisplayName("휴일 리스트 조회")
    void getHolidays() {
        // given
        String[] names = {"신정", "어린이날", "크리스마스"};
        String[] dates = {"20250101", "20250505", "20251225"};

        for (int i = 0; i < names.length; i++) {
            Holiday holiday = Holiday.createHoliday(names[i], dates[i]);
            holidayRepository.save(holiday);
        }

        double random = Math.random();
        int idx = (int)(random * 2);    // 0~2

        // when
        List<Holiday> holidays = holidayRepository.findHolidays();  // order로 정렬하므로 배열 idx와 같아야함

        // then
        assertThat(holidays.size()).isEqualTo(names.length);
        assertThat(holidays.get(idx).getName()).isEqualTo(names[idx]);
        assertThat(holidays.get(idx).getDate()).isEqualTo(dates[idx]);
    }

    @Test
    @DisplayName("기간에 해당하는 휴일만 나오는지 조회 (정상 케이스)")
    void getHolidaysByDate() {
        // given
        String[] names = {"신정", "어린이날", "크리스마스"};
        String[] dates = {"20250101", "20250505", "20251225"};

        for (int i = 0; i < names.length; i++) {
            Holiday holiday = Holiday.createHoliday(names[i], dates[i]);
            holidayRepository.save(holiday);
        }

        // when
        List<Holiday> holidays = holidayRepository.findHolidaysByStartEndDate("20241201", "20250131");

        // then
        assertThat(holidays.size()).isEqualTo(1);
        assertThat(holidays.get(0).getName()).isEqualTo("신정");
        assertThat(holidays.get(0).getDate()).isEqualTo("20250101");
    }

    @Test
    @DisplayName("기간에 해당하는 휴일만 나오는지 조회 (경계값 케이스)")
    void getHolidaysByDateBoundary() {
        // given
        String[] names = {"신정", "어린이날", "크리스마스"};
        String[] dates = {"20250101", "20250505", "20251225"};

        for (int i = 0; i < names.length; i++) {
            Holiday holiday = Holiday.createHoliday(names[i], dates[i]);
            holidayRepository.save(holiday);
        }

        // when
        List<Holiday> holidayLeft = holidayRepository.findHolidaysByStartEndDate("20250101", "20250504");
        List<Holiday> holidayRight = holidayRepository.findHolidaysByStartEndDate("20250102", "20250505");
        List<Holiday> holidayNo = holidayRepository.findHolidaysByStartEndDate("20250102", "20250504");

        // then
        assertThat(holidayLeft.size()).isEqualTo(1);
        assertThat(holidayLeft.get(0).getName()).isEqualTo("신정");
        assertThat(holidayLeft.get(0).getDate()).isEqualTo("20250101");

        assertThat(holidayRight.size()).isEqualTo(1);
        assertThat(holidayRight.get(0).getName()).isEqualTo("어린이날");
        assertThat(holidayRight.get(0).getDate()).isEqualTo("20250505");

        assertThat(holidayNo.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("휴일 삭제")
    void deleteHoliday() {
        // given
        String name = "신정";
        String date = "20250101";

        Holiday holiday = Holiday.createHoliday(name, date);
        holidayRepository.save(holiday);

        // when
        holidayRepository.delete(holiday);
        Holiday findHoliday = holidayRepository.findHoliday(holiday.getSeq());

        // then
        assertThat(Objects.isNull(findHoliday)).isEqualTo(true);
    }
}