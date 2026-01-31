package kr.ac.knu.cse.domain.authorization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import kr.ac.knu.cse.domain.due.Dues;
import kr.ac.knu.cse.domain.due.DuesRepository;
import kr.ac.knu.cse.domain.role.RoleType;
import kr.ac.knu.cse.domain.student.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorizationPolicyTest {

    @Mock
    private DuesRepository duesRepository;

    @InjectMocks
    private AuthorizationPolicy authorizationPolicy;

    @DisplayName("Has ROLE_USER when student dues have not been paid.")
    @Test
    void userWithoutDues_remainsUser() {
        // given
        Student student = Student.of("홍길동", "2022111111");

        given(
                duesRepository.existsByStudentIdAndRemainingSemestersGreaterThanEqual(
                        any(),
                        eq(Dues.MIN_VALID_REMAINING_SEMESTERS)
                )
        ).willReturn(false);


        // when
        RoleType result = authorizationPolicy.calculate(student);

        // then
        assertThat(result).isEqualTo(RoleType.ROLE_USER);
    }

    @DisplayName("Has ROLE_MEMBER when student dues have been paid.")
    @Test
    void userWithDues_becomesMember() {
        // given
        Student student = Student.of("홍길동", "2022111111");

        given(
                duesRepository.existsByStudentIdAndRemainingSemestersGreaterThanEqual(
                        any(),
                        eq(Dues.MIN_VALID_REMAINING_SEMESTERS)
                )
        ).willReturn(true);

        // when
        RoleType result = authorizationPolicy.calculate(student);

        // then
        assertThat(result).isEqualTo(RoleType.ROLE_MEMBER);
    }

    @DisplayName("Executives have the ROLE_EXEC role.")
    @Test
    void execWithDues_remainsExec() {
        // given
        Student student = Student.of("홍길동", "2022111111");
        student.grantRole(RoleType.ROLE_EXEC);

        given(
                duesRepository.existsByStudentIdAndRemainingSemestersGreaterThanEqual(
                        any(),
                        eq(Dues.MIN_VALID_REMAINING_SEMESTERS)
                )
        ).willReturn(true);

        // when
        RoleType result = authorizationPolicy.calculate(student);

        // then
        assertThat(result).isEqualTo(RoleType.ROLE_EXEC);
    }

    @DisplayName("The system super administrator has the ROLE_ADMIN role.")
    @Test
    void adminWithDues_remainsAdmin() {
        // given
        Student student = Student.of("홍길동", "2022111111");
        student.grantRole(RoleType.ROLE_ADMIN);

        given(
                duesRepository.existsByStudentIdAndRemainingSemestersGreaterThanEqual(
                        any(),
                        eq(Dues.MIN_VALID_REMAINING_SEMESTERS)
                )
        ).willReturn(true);

        // when
        RoleType result = authorizationPolicy.calculate(student);

        // then
        assertThat(result).isEqualTo(RoleType.ROLE_ADMIN);
    }
}