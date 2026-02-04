package kr.ac.knu.cse.global.exception;

import static kr.ac.knu.cse.global.exception.common.CommonErrorCode.*;

import kr.ac.knu.cse.global.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex
    ) {

        HttpStatus status = resolveStatus(ex);

        ErrorResponse response = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(status).body(response);
    }

    private HttpStatus resolveStatus(BusinessException ex) {
        String code = ex.getCode();

        if (code.startsWith("AUTH_")) {
            return HttpStatus.UNAUTHORIZED;
        }

        if (code.startsWith("PROVISION_")) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        if (code.startsWith("COMMON_")) {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException() {
        ErrorResponse response = ErrorResponse.builder()
                .code(INTERNAL_ERROR.code())
                .message(INTERNAL_ERROR.message())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
