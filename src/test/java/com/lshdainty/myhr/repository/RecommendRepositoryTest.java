package com.lshdainty.myhr.repository;

import com.lshdainty.myhr.domain.Recommend;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(RecommendRepository.class)
@Transactional
@DisplayName("JPA 권장휴가 레포지토리 테스트")
class RecommendRepositoryTest {
    @Autowired
    private RecommendRepository recommendRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("권장휴가 리스트 조회")
    void getRecommends() {
        // given
        String[] names = {"1월 권장휴가", "2월 권장휴가", "3월 권장휴가"};
        String[] dates = {"20250101", "20250201", "20250301"};

        for (int i = 0; i < names.length; i++) {
            Recommend recommend = Recommend.addRecommend(names[i], dates[i]);
            em.persist(recommend);
        }

        double random = Math.random();
        int idx = (int)(random * 2);    // 0~2

        // when
        List<Recommend> recommends = recommendRepository.findRecommends();  // order로 정렬하므로 배열 idx와 같아야함

        // then
        assertThat(recommends.size()).isEqualTo(names.length);
        assertThat(recommends.get(idx).getName()).isEqualTo(names[idx]);
        assertThat(recommends.get(idx).getDate()).isEqualTo(dates[idx]);
    }

    @Test
    @DisplayName("기간에 해당하는 권장휴가만 나오는지 조회 (정상 케이스)")
    void getHolidaysByDate() {
        // given
        String[] names = {"1월 권장휴가", "2월 권장휴가", "3월 권장휴가"};
        String[] dates = {"20250101", "20250201", "20250301"};

        for (int i = 0; i < names.length; i++) {
            Recommend recommend = Recommend.addRecommend(names[i], dates[i]);
            em.persist(recommend);
        }

        // when
        List<Recommend> recommends = recommendRepository.findRecommendsByStartEndDate("20241201", "20250131");

        // then
        assertThat(recommends.size()).isEqualTo(1);
        assertThat(recommends.get(0).getName()).isEqualTo("1월 권장휴가");
        assertThat(recommends.get(0).getDate()).isEqualTo("20250101");
    }

    @Test
    @DisplayName("기간에 해당하는 권장휴가만 나오는지 조회 (경계값 케이스)")
    void getHolidaysByDateBoundary() {
        // given
        String[] names = {"1월 권장휴가", "2월 권장휴가", "3월 권장휴가"};
        String[] dates = {"20250101", "20250201", "20250301"};

        for (int i = 0; i < names.length; i++) {
            Recommend recommend = Recommend.addRecommend(names[i], dates[i]);
            em.persist(recommend);
        }

        // when
        List<Recommend> recommendLeft = recommendRepository.findRecommendsByStartEndDate("20250101", "20250131");
        List<Recommend> recommendRight = recommendRepository.findRecommendsByStartEndDate("20250102", "20250201");
        List<Recommend> recommendNo = recommendRepository.findRecommendsByStartEndDate("20250102", "20250131");

        // then
        assertThat(recommendLeft.size()).isEqualTo(1);
        assertThat(recommendLeft.get(0).getName()).isEqualTo("1월 권장휴가");
        assertThat(recommendLeft.get(0).getDate()).isEqualTo("20250101");

        assertThat(recommendRight.size()).isEqualTo(1);
        assertThat(recommendRight.get(0).getName()).isEqualTo("2월 권장휴가");
        assertThat(recommendRight.get(0).getDate()).isEqualTo("20250201");

        assertThat(recommendNo.size()).isEqualTo(0);
    }
}