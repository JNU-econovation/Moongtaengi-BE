package econovation.moongtaengi.study.domain.submission;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SubmissionErrorCode implements ErrorCode {
    INVALID_SUBMISSION_INFO(HttpStatus.BAD_REQUEST, "SM_001", "제출 정보가 올바르지 않습니다."),

    CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "SM_002", "제출 내용은 %d자를 초과할 수 없습니다."),

    INVALID_ATTACHMENT_URL(HttpStatus.BAD_REQUEST, "SM_003", "잘못된 첨부파일 URL 형식입니다."),

    CREATE_ARGUMENT_MISSING(HttpStatus.BAD_REQUEST, "SM_004", "제출물 생성 과정에서 필수 인자가 누락되었습니다."),

    ATTACHMENT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "SM_005", "첨부파일은 최대 %d개까지만 업로드 가능합니다."),

    ALREADY_SUBMITTED(HttpStatus.CONFLICT, "SM_006", "이미 제출한 내역이 존재합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
