package econovation.moongtaengi.global.exception;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("비즈니스 예외 발생: {} - {}", e.getErrorCode().getCode(), e.getMessage());

        ErrorResponse response = ErrorResponse.of(e.getErrorCode());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(fieldError -> String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining("; "));

        log.warn("유효성 검사 실패: {}", errorMessage);

        ErrorResponse response = ErrorResponse.of(
                GlobalErrorCode.INVALID_INPUT_VALUE.getStatus(),
                GlobalErrorCode.INVALID_INPUT_VALUE.getCode(),
                errorMessage
        );

        return ResponseEntity
                .status(GlobalErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("예상치 못한 오류 발생", e);

        ErrorResponse response = ErrorResponse.of(GlobalErrorCode.INTERNAL_SERVER_ERROR);

        return ResponseEntity
                .status(GlobalErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(response);
    }
}
