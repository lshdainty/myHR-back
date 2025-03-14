package com.lshdainty.myhr.repository;

import com.lshdainty.myhr.domain.Dues;
import com.lshdainty.myhr.domain.DuesType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(DuesRepository.class)
@Transactional
@DisplayName("JPA 회비 레포지토리 테스트")
class DuesRepositoryTest {
    @Autowired
    private DuesRepository duesRepository;

    @Test
    @DisplayName("회비 저장")
    void save() {
        // given
        String userName = "이서준";
        int amount = 10000;
        DuesType type = DuesType.PLUS;
        String date = "20250120";
        String detail = "1월 생일 회비";

        Dues dues = Dues.createDues(userName, amount, type, date, detail);

        // when
        duesRepository.save(dues);

        Dues findDues = duesRepository.findById(dues.getSeq());

        // then
        assertThat(findDues).isEqualTo(dues);
        assertThat(findDues.getUserName()).isEqualTo(userName);
        assertThat(findDues.getAmount()).isEqualTo(amount);
        assertThat(findDues.getType()).isEqualTo(type);
        assertThat(findDues.getDate()).isEqualTo(date);
        assertThat(findDues.getDetail()).isEqualTo(detail);
    }

    @Test
    @DisplayName("단일 회비 조회")
    void findById() {
        // given
        String userName = "이서준";
        int amount = 10000;
        DuesType type = DuesType.PLUS;
        String date = "20250120";
        String detail = "1월 생일 회비";

        Dues dues = Dues.createDues(userName, amount, type, date, detail);
        duesRepository.save(dues);

        // when
        Dues findDues = duesRepository.findById(dues.getSeq());

        // then
        assertThat(findDues).isEqualTo(dues);
        assertThat(findDues.getUserName()).isEqualTo(userName);
        assertThat(findDues.getAmount()).isEqualTo(amount);
        assertThat(findDues.getType()).isEqualTo(type);
        assertThat(findDues.getDate()).isEqualTo(date);
        assertThat(findDues.getDetail()).isEqualTo(detail);
    }

    @Test
    @DisplayName("전체 회비 목록 조회")
    void getDues() {
        // given
        String[] names = {"이서준" ,"조민서" ,"이준우"};
        int[] amounts = {10000, 80000, 10000};
        DuesType[] types = {DuesType.PLUS, DuesType.MINUS, DuesType.PLUS};
        String[] dates = {"20250104", "20250131", "20250204"};
        String[] details = {"생일비", "생일비 출금", "생일비"};

        for (int i = 0; i < names.length; i++) {
            Dues dues = Dues.createDues(names[i], amounts[i], types[i], dates[i], details[i]);
            duesRepository.save(dues);
        }

        double random = Math.random();
        int idx = (int)(random * 2);    // 0~2

        // when
        List<Dues> dues = duesRepository.findDues();    // order로 정렬하므로 배열 idx와 같아야함

        // then
        assertThat(dues.size()).isEqualTo(names.length);
        assertThat(dues.get(idx).getUserName()).isEqualTo(names[idx]);
        assertThat(dues.get(idx).getAmount()).isEqualTo(amounts[idx]);
        assertThat(dues.get(idx).getType()).isEqualTo(types[idx]);
        assertThat(dues.get(idx).getDate()).isEqualTo(dates[idx]);
        assertThat(dues.get(idx).getDetail()).isEqualTo(details[idx]);
    }

    @Test
    @DisplayName("년도에 해당하는 회비 목록 조회")
    void getDuesByYear() {
        // given
        String[] names = {"이서준" ,"조민서" ,"이준우"};
        int[] amounts = {10000, 80000, 10000};
        DuesType[] types = {DuesType.PLUS, DuesType.MINUS, DuesType.PLUS};
        String[] dates = {"20241204", "20250131", "20250204"};
        String[] details = {"생일비", "생일비 출금", "생일비"};

        for (int i = 0; i < names.length; i++) {
            Dues dues = Dues.createDues(names[i], amounts[i], types[i], dates[i], details[i]);
            duesRepository.save(dues);
        }

        // when
        List<Dues> dues = duesRepository.findDuesByYear("2025");

        // then
        assertThat(dues.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("회비 삭제")
    void deleteDues() {
        // given
        String userName = "이서준";
        int amount = 10000;
        DuesType type = DuesType.PLUS;
        String date = "20250120";
        String detail = "1월 생일 회비";

        Dues dues = Dues.createDues(userName, amount, type, date, detail);
        duesRepository.save(dues);

        // when
        duesRepository.delete(dues);
        Dues findDues = duesRepository.findById(dues.getSeq());

        // then
        assertThat(Objects.isNull(findDues)).isEqualTo(true);
    }
}
