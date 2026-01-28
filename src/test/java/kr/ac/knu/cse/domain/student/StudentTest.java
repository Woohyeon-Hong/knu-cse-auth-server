package kr.ac.knu.cse.domain.student;

import static kr.ac.knu.cse.domain.role.RoleType.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StudentTest {

    @DisplayName("이름과 학번을 입력받아, ROLE_USER 권한을 가진 학생을 생성한다.")
    @Test
    void of() {
        //given && when
        Student student = Student.of("이름", "test@knu.ac.kr");

        //then
        assertThat(student.getId()).isNull();
        assertThat(student.getRole()).isEqualTo(ROLE_USER);
        assertThat(student.getMajor()).isEqualTo("UNKNOWN");
    }

    @DisplayName("전공이 입력된 경우, true를 반환한다.")
    @Test
    void hasConfirmedMajor() {
        //given
        Student student = Student.of("이름", "test@knu.ac.kr");
        student.replaceMajor("컴퓨터학부");

        //when && then
        assertThat(student.hasConfirmedMajor()).isTrue();
    }

    @DisplayName("학번이 입력된 경우, true를 반환한다.")
    @Test
    void hasConfirmedStudentNumber() {
        //given
        Student student = Student.of("이름", "test@knu.ac.kr");
        student.replaceStudentNumber("2022111111");

        //when && then
        assertThat(student.hasConfirmedStudentNumber()).isTrue();
    }
}