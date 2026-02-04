package kr.ac.knu.cse.global.exception.provisioning;

import kr.ac.knu.cse.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProvisioningErrorCode implements ErrorCode {

    PROVIDER_HAS_NO_STUDENT("PROVISION_001", "Provider와 연결된 Student가 존재하지 않습니다."),
    DUPLICATE_STUDENT_NUMBER("PROVISION_002", "중복된 학번이 발생했습니다."),
    TEMP_STUDENT_CREATION_FAILED("PROVISION_003", "임시 학번 생성에 실패했습니다.");

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
