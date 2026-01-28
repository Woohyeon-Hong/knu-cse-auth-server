package kr.ac.knu.cse.domain.student;

import static kr.ac.knu.cse.domain.role.RoleType.ROLE_USER;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.ac.knu.cse.domain.role.RoleType;
import kr.ac.knu.cse.global.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
### Student

### 매핑 테이블

`student`

### 필드

| 필드명 | 타입 | 설명 |
| --- | --- | --- |
| id | Long | PK, 컬럼명 `student_id` |
| role | RoleType | 최상위 조직 권한 |
| major | String | 전공, 최대 50자 |
| name | String | 이름, 최대 50자 |
| studentNumber | String | 학번, 최대 15자, 유니크 |
| createdAt | LocalDateTime | 생성 시각 |
| updatedAt | LocalDateTime | 수정 시각 |

### 제약 조건

- `student_number`는 시스템 전역에서 유니크하다.
- 계정 상태(status) 개념은 포함하지 않는다.

### 역할 및 책임

- Student는 **조직 내 최상위 권한 상태만 저장**한다.
- `ROLE_MEMBER`는 Student에 저장하지 않는다.
- Provider와의 관계는 Provider가 `student_id`를 보유함으로써 표현된다.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "student")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Student extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "student_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private RoleType role;
    @Column(name = "major", nullable = false, length = 50)
    private String major;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "student_number", nullable = false, length = 15, unique = true)
    private String studentNumber;

    public static Student of(
            String name,
            String studentNumber
    ) {
        return new Student(
                null,
                ROLE_USER,
                "UNKNOWN",
                name,
                studentNumber
        );
    }

    public void grantRole(RoleType role) {
        this.role = role;
    }

    public void revokeToUser() {
        this.role = ROLE_USER;
    }

    public void replaceMajor(String major) {
        this.major = major;
    }

    public void  replaceStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public boolean hasConfirmedMajor() {
        return !major.equals("UNKNOWN");
    }

    public boolean hasConfirmedStudentNumber() {
        return !studentNumber.contains("@");
    }
}
