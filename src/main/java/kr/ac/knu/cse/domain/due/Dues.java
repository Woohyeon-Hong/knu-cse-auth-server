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

**Mapping Table**: `dues`

| Field | Type | Description |
| --- | --- | --- |
| id | Long | PK, column name `dues_id` |
| studentId | Long | FK, column name `student_id` |
| depositorName | String | Depositor name, max 50 characters |
| amount | int | Paid amount |
| remainingSemesters | int | Remaining valid semesters |
| submittedAt | LocalDateTime | Dues payment timestamp |

### Role

- Dues is the **sole source of truth** for determining the `ROLE_MEMBER` role.
- The `ROLE_MEMBER` value itself is not stored in the database.
- `AuthorizationPolicy` calculates `ROLE_MEMBER` based on the Dues status.
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
