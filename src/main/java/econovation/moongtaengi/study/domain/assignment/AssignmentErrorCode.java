package econovation.moongtaengi.study.domain.assignment;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AssignmentErrorCode implements ErrorCode {
    INVALID_ASSIGNMENT_INFO(HttpStatus.BAD_REQUEST, "AS_001", "과제 정보가 올바르지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
