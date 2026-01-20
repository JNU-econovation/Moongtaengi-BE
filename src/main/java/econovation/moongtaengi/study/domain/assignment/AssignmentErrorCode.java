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

    INVALID_PROCESS_ID(HttpStatus.BAD_REQUEST, "AS_004","유효하지 않은 프로세스 ID 입니다."),

    ASSIGNMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "AS_005", "존재하지 않는 과제입니다."),

    CANNOT_APPROVE_NOT_SUBMITTED(HttpStatus.BAD_REQUEST, "AS_006", "제출되지 않은 과제는 승인할 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
