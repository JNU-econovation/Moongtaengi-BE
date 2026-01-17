package econovation.moongtaengi.study.domain.assignment;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AssignmentErrorCode implements ErrorCode {
    INVALID_ASSIGNMENT_INFO(HttpStatus.BAD_REQUEST, "AS_001", "과제 정보가 올바르지 않습니다."),

    NO_MANAGEMENT_PERMISSION(HttpStatus.FORBIDDEN, "AS_002", "과제 관리 권한이 없습니다."),

    ASSIGNMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "AS_003", "해당 프로세스에 이미 할당된 과제가 존재합니다."),

    INVALID_PROCESS_ID(HttpStatus.BAD_REQUEST, "AS_004","유효하지 않은 프로세스 ID 입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
