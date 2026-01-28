package kr.ac.knu.cse.domain.role;

/*
### RoleType

- `ROLE_USER`
- `ROLE_EXEC`
- `ROLE_FINANCE`
- `ROLE_ADMIN`
- `ROLE_MEMBER`

> ROLE_MEMBER는 저장되는 권한이 아니라,
>
>
> Dues 상태를 기반으로 AuthorizationPolicy에서 계산되는 **파생 권한**이다.
>
> 계산 결과는 토큰에만 포함된다.
>
 */

public enum RoleType {
    ROLE_USER,
    ROLE_EXEC,
    ROLE_FINANCE,
    ROLE_ADMIN,
    ROLE_MEMBER
}
