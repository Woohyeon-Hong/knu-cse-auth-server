package kr.ac.knu.cse.domain.due;

import static kr.ac.knu.cse.domain.due.Dues.MIN_VALID_REMAINING_SEMESTERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import kr.ac.knu.cse.support.JpaIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@JpaIntegrationTest
class DuesRepositoryTest {

    @Autowired
    DuesRepository duesRepository;
    @Autowired
    EntityManager em;

    @DisplayName("기준 학기 이상으로 잔여 학기가 남아있으면, true를 반환한다.")
    @Test
    void existsByStudentIdAndRemainingSemestersGreaterThanEqual() {
        //given
        duesRepository.save(Dues.of(
                1L,
                "name",
                10000,
                1,
                LocalDateTime.now())
        );

        em.flush();
        em.clear();

        //when && then
        assertThat(
                duesRepository.existsByStudentIdAndRemainingSemestersGreaterThanEqual(
                        1L,
                        MIN_VALID_REMAINING_SEMESTERS
                )
        ).isTrue();
    }
}