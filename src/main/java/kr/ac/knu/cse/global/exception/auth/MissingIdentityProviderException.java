package kr.ac.knu.cse.global.exception.auth;

import kr.ac.knu.cse.global.exception.BusinessException;
import kr.ac.knu.cse.global.exception.ErrorCode;

public class MissingIdentityProviderException extends BusinessException {
    public MissingIdentityProviderException() {
        super(AuthErrorCode.MISSING_IDENTITY_PROVIDER);
    }
}
