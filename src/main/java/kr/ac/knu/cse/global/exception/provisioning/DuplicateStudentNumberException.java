package kr.ac.knu.cse.global.exception.provisioning;

import kr.ac.knu.cse.global.exception.BusinessException;
import kr.ac.knu.cse.global.exception.ErrorCode;

public class DuplicateStudentNumberException extends BusinessException {

    public DuplicateStudentNumberException() {
        super(ProvisioningErrorCode.DUPLICATE_STUDENT_NUMBER);
    }
}
