package kr.ac.knu.cse.domain.provider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.ac.knu.cse.global.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
### Provider

### 매핑 테이블

`provider`

### 필드

| 필드명 | 타입 | 설명 |
| --- | --- | --- |
| id | Long | PK, 컬럼명 `provider_id` |
| email | String | Provider 기준 이메일, 유니크 |
| providerName | String | 인증 제공자 이름 (예: GOOGLE) |
| providerKey | String | Provider 내부 사용자 식별자 |
| studentId | Long | FK, 컬럼명 `student_id`, NULL 허용 |
| createdAt | LocalDateTime | 생성 시각 |
| updatedAt | LocalDateTime | 수정 시각 |

### 제약 조건

- `email`은 유니크하다.
- `student_id`는 NULL 허용이다.
- Student 삭제 시 Provider의 `student_id`는 NULL로 설정된다.

### 역할 및 책임

- Provider는 **외부 인증 계정(OAuth 계정)을 표현하는 엔티티**다.
- Provider는 Student와 **느슨하게 연결**된다.
- 인증 계정은 존재하지만, 내부 사용자(Student)와 아직 연결되지 않은 상태를 허용한다.
- Provider는 **OAuth 인증 식별 정보(email, providerKey)를 보관**한다.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(
        name = "provider",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "provider_name_key_uq",
                        columnNames = {"provider_name", "provider_key"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Provider extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "provider_id")
    private Long id;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;
    @Column(name = "provider_name", nullable = false, length = 255)
    private String providerName;
    @Column(name = "provider_key", nullable = false, length = 255)
    private String providerKey;
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    public static Provider of(
            String email,
            String providerName,
            String providerKey,
            Long studentId
    ) {
        return new Provider(
                null,
                email,
                providerName,
                providerKey,
                studentId
        );
    }
}
