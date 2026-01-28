package kr.ac.knu.cse.domain.authorization;

import static kr.ac.knu.cse.domain.due.Dues.MIN_VALID_REMAINING_SEMESTERS;

import kr.ac.knu.cse.domain.due.DuesRepository;
import kr.ac.knu.cse.domain.role.RoleType;
import kr.ac.knu.cse.domain.student.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationPolicy {

    private final DuesRepository duesRepository;

    public RoleType calculate(Student student) {
        RoleType baseRole = student.getRole();

        boolean isMember = isMember(student);

        // 조직 권한이 USER일 때만 MEMBER로 승격
        if (isMember && baseRole == RoleType.ROLE_USER) {
            return RoleType.ROLE_MEMBER;
        }

        return baseRole;
    }

    private boolean isMember(Student student) {
        return duesRepository.existsByStudentIdAndRemainingSemestersGreaterThanEqual(
                student.getId(),
                MIN_VALID_REMAINING_SEMESTERS
        );
    }
}
