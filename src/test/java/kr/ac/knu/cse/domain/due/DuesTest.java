package kr.ac.knu.cse.domain.due;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DuesTest {

    @DisplayName("Student dues are valid when remaining semesters exist.")
    @Test
    void isValid() {
        assertThat(
                Dues.of(
                        1L,
                        "test",
                        10000,
                        4,
                        LocalDateTime.now()
                ).isValid()
        ).isTrue();

        assertThat(
                Dues.of(
                        1L,
                        "test",
                        10000,
                        0,
                        LocalDateTime.now()
                ).isValid()
        ).isFalse();
    }
}