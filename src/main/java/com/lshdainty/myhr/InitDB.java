package com.lshdainty.myhr;

import com.lshdainty.myhr.domain.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initSetMember();
        initService.initSetHoliday();
        initService.initSetRecommend();
        initService.initSetVacation();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void initSetMember() {
            saveMember("이서준", "19700723", "9 ~ 6", "ADMIN", "N");
            saveMember("김서연", "19701026", "8 ~ 5", "BP", "N");
            saveMember("김지후", "19740115", "10 ~ 7", "BP", "Y");
            saveMember("이준우", "19800430", "9 ~ 6", "BP", "N");
            saveMember("조민서", "19921220", "9 ~ 6", "ADMIN", "N");
            saveMember("이하은", "18850902", "8 ~ 5", "ADMIN", "N");
        }

        public void initSetHoliday() {
            saveHoliday("신정", "20250101");
            saveHoliday("임시공휴일(설날)", "20250127");
            saveHoliday("설날연휴", "20250128");
            saveHoliday("설날", "20250129");
            saveHoliday("설날연휴", "20250130");
            saveHoliday("삼일절", "20250301");
            saveHoliday("대체공휴일(삼일절)", "20250303");
            saveHoliday("근로자의 날", "20250501");
            saveHoliday("어린이날", "20250505");
            saveHoliday("대체공휴일(석가탄신일)", "20250506");
            saveHoliday("현충일", "20250606");
            saveHoliday("광복절", "20250815");
            saveHoliday("개천절", "20251003");
            saveHoliday("추석연휴", "20251005");
            saveHoliday("추석", "20251006");
            saveHoliday("추석연휴", "20251007");
            saveHoliday("대체공휴일(추석)", "20251008");
            saveHoliday("한글날", "20251009");
            saveHoliday("크리스마스", "20251225");
        }

        public void initSetRecommend() {
            saveRecommend("권장휴가", "20250131");
            saveRecommend("권장휴가", "20250304");
            saveRecommend("권장휴가", "20250404");
            saveRecommend("권장휴가", "20250502");
            saveRecommend("권장휴가", "20250523");
            saveRecommend("권장휴가", "20250704");
            saveRecommend("권장휴가", "20250814");
            saveRecommend("권장휴가", "20250905");
            saveRecommend("권장휴가", "20251010");
            saveRecommend("권장휴가", "20251114");
        }

        public void initSetVacation() {
            saveVacation(1L, "1분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(2L, "1분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(3L, "1분기 휴가", "작년 하루 사용", VacationType.BASIC, 24f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(4L, "1분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(5L, "1분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(6L, "1분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(1L, "2분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(2L, "2분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(3L, "2분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(4L, "2분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(5L, "2분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(6L, "2분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(1L, "3분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(2L, "3분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(3L, "3분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(4L, "3분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(5L, "3분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(6L, "3분기 휴가", "", VacationType.BASIC, 32.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(1L, "4분기 휴가", "", VacationType.BASIC, 24.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(2L, "4분기 휴가", "", VacationType.BASIC, 24.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(3L, "4분기 휴가", "", VacationType.BASIC, 24.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(4L, "4분기 휴가", "", VacationType.BASIC, 24.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(5L, "4분기 휴가", "", VacationType.BASIC, 24.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(6L, "4분기 휴가", "", VacationType.BASIC, 24.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(1L, "출산 휴가", "출산 추가 휴가 부여", VacationType.ADDED, 80.0f, LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 23, 59, 59).plusMonths(6));
            saveVacation(1L, "OT 정산", "월마감 지원", VacationType.ADDED, 3.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(1L, "OT 정산", "월마감 지원", VacationType.ADDED, 1.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(2L, "OT 정산", "월마감 지원", VacationType.ADDED, 2.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(4L, "OT 정산", "월마감 지원", VacationType.ADDED, 6.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(1L, "OT 정산", "월마감 지원", VacationType.ADDED, 6.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(1L, "OT 정산", "월마감 지원", VacationType.ADDED, 5.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(2L, "결혼 휴가", "결혼 추가 휴가 부여", VacationType.ADDED, 40.0f, LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 23, 59, 59).plusMonths(6));
            saveVacation(3L, "OT 정산", "월마감 지원", VacationType.ADDED, 1.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(1L, "OT 정산", "월마감 지원", VacationType.ADDED, 1.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(5L, "OT 정산", "월마감 지원", VacationType.ADDED, 2.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(6L, "OT 정산", "월마감 지원", VacationType.ADDED, 10.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(1L, "OT 정산", "월마감 지원", VacationType.ADDED, 1.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(2L, "OT 정산", "월마감 지원", VacationType.ADDED, 2.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
            saveVacation(3L, "OT 정산", "월마감 지원", VacationType.ADDED, 1.0f, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
        }

        public void initSetSchedule() {

        }

        public void saveMember(String name, String birth, String workTime, String employ, String lunar) {
            User user = User.addUser(name, birth, employ, workTime, lunar);
            em.persist(user);
        }

        public void saveHoliday(String name, String date) {
            Holiday holiday = Holiday.addHoliday(name, date);
            em.persist(holiday);
        }

        public void saveRecommend(String name, String date) {
            Recommend recommend = Recommend.addRecommend(name, date);
            em.persist(recommend);
        }

        public void saveVacation(Long userNo, String name, String description, VacationType type, float grantedTime, LocalDateTime expirationDate) {
            User user = em.find(User.class, userNo);
            Vacation vacation = Vacation.addVacation(user, name, description, type, grantedTime, expirationDate, 0L, "127.0.0.1");
            em.persist(vacation);
        }
    }
}
