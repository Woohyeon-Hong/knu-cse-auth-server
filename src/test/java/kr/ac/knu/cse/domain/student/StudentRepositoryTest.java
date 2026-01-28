package kr.ac.knu.cse.domain.student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import kr.ac.knu.cse.support.JpaIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@JpaIntegrationTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    EntityManager em;

    void flushAndClear() {
        em.flush();
        em.clear();
    }

    @DisplayName("학생을 생성할 때 학번이 중복되면, DB 반영 시점에 예외가 발생한다.")
    @Test
    void of() {
        //given
        String studentNumber = "2022111111";
        Student student1 = Student.of("학생1",  studentNumber);

       studentRepository.save(student1);
        flushAndClear();

        Student student2 = Student.of("학생2", studentNumber);

        //when && then
        assertThatThrownBy(() -> {
            studentRepository.save(student2);
            flushAndClear();
        });
    }

    @DisplayName("학번을 기반으로 학생을 조회한다.")
    @Test
    void findByStudentNumber() {
        //given
        String studentNumber = "2022111111";
        Student student = Student.of("학생1",  studentNumber);

        Student saved = studentRepository.save(student);
        flushAndClear();

        //when
        Optional<Student> result = studentRepository.findByStudentNumber(studentNumber);

        //then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(saved);
    }

    @DisplayName("해당 학번을 가지는 학생이 있으면, true를 반환한다.")
    @Test
    void existsByStudentNumber() {
        //given
        String studentNumber = "2022111111";
        Student student = Student.of("학생1",  studentNumber);

        studentRepository.save(student);
        flushAndClear();

        //when && then
        assertThat(
                studentRepository.existsByStudentNumber(studentNumber)
        ).isTrue();
    }
}