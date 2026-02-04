package kr.ac.knu.cse.global.exception.auth;

import kr.ac.knu.cse.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    INVALID_OIDC_USER("AUTH_001", "인증 사용자 정보가 올바르지 않습니다."),
    MISSING_EMAIL("AUTH_002", "이메일 정보가 누락되었습니다."),
    INVALID_EMAIL_DOMAIN("AUTH_003", "허용되지 않은 이메일 도메인입니다."),
    MISSING_IDENTITY_PROVIDER("AUTH_004", "인증 제공자 정보가 누락되었습니다.");

    private final String code;
    private final String message;
    @Override
    public String code() {
        return null;
    }

    @Override
    public String message() {
        return null;
    }
}
