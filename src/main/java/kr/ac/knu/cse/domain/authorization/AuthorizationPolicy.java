package kr.ac.knu.cse.domain.authorization;

import static kr.ac.knu.cse.domain.due.Dues.MIN_VALID_REMAINING_SEMESTERS;

import kr.ac.knu.cse.domain.due.DuesRepository;
import kr.ac.knu.cse.domain.role.RoleType;
import kr.ac.knu.cse.domain.student.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/*
## AuthorizationPolicy

### Meaning

The AuthorizationPolicy domain calculates the final effective roles of a user
by taking the Student and Dues domains as input.
This domain does not persist data and contains only pure rules and calculation logic.

### Responsibilities

- Applies hierarchical role rules based on the organizational role (Student).
- Determines whether `ROLE_MEMBER` should be granted based on the Dues status.
- Calculates the final set of usable roles.
- Does not persist calculation results; roles are recalculated whenever needed.
*/

@Component
@RequiredArgsConstructor
public class AuthorizationPolicy {

    private final DuesRepository duesRepository;

    public RoleType calculate(Student student) {
        RoleType baseRole = student.getRole();

        boolean isMember = isMember(student);

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
