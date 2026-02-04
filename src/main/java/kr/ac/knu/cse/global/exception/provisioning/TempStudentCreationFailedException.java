package kr.ac.knu.cse.global.exception.provisioning;

import kr.ac.knu.cse.global.exception.BusinessException;
import kr.ac.knu.cse.global.exception.ErrorCode;

public class TempStudentCreationFailedException extends BusinessException {
    public TempStudentCreationFailedException() {
        super(ProvisioningErrorCode.TEMP_STUDENT_CREATION_FAILED);
    }
}
