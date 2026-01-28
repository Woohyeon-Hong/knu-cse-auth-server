package kr.ac.knu.cse.domain.due;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.ac.knu.cse.global.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
### Dues

**매핑 테이블**: `dues`

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| id | Long | PK, 컬럼명 `dues_id` |
| studentId | Long | FK, 컬럼명 `student_id` |
| depositorName | String | 납부자 이름, 최대 50자 |
| amount | int | 납부 금액 |
| remainingSemesters | int | 남은 유효 학기 수 |
| submittedAt | LocalDateTime | 회비 납부 시각 |

### 역할

- Dues는 `ROLE_MEMBER` 권한 판단의 **유일한 근거 데이터**다.
- `ROLE_MEMBER` 값 자체는 DB에 저장하지 않는다.
- `AuthorizationPolicy`에서 Dues 상태를 기반으로 `ROLE_MEMBER`를 계산한다.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "dues")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Dues extends BaseTimeEntity {

    public static final int MIN_VALID_REMAINING_SEMESTERS = 1;

    @Id
    @GeneratedValue
    @Column(name = "dues_id")
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "depositor_name", nullable = false, length = 50)
    private String depositorName;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "remaining_semesters", nullable = false)
    private int remainingSemesters;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    public static Dues of(
            Long studentId,
            String depositorName,
            int amount,
            int remainingSemesters,
            LocalDateTime submittedAt
    ) {
        return new Dues(
                null,
                studentId,
                depositorName,
                amount,
                remainingSemesters,
                submittedAt
        );
    }

    public boolean isValid() {
        return remainingSemesters >= MIN_VALID_REMAINING_SEMESTERS;
    }

}
