package kr.ac.knu.cse.global.exception.auth;

import static kr.ac.knu.cse.global.exception.auth.AuthErrorCode.ACCOUNT_DISABLED;

import kr.ac.knu.cse.global.exception.BusinessException;

public class AccountDisabledException extends BusinessException {
    public AccountDisabledException() {
        super(ACCOUNT_DISABLED);
    }
}
