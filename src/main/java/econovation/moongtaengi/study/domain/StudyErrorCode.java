package econovation.moongtaengi.study.domain;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StudyErrorCode implements ErrorCode {
    //스터디
    STUDY_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDY_001", "존재하지 않는 스터디입니다."),

    // 스터디 생성 제한
    STUDY_CREATION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "STUDY_002", "스터디는 최대 %d개까지만 생성 가능합니다."),

    // 스터디 이름
    NAME_NOT_BLANK(HttpStatus.BAD_REQUEST, "STUDY_003", "스터디 이름은 비어있을 수 없습니다."),
    NAME_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "STUDY_004", "스터디 이름은 %d자 이상 %d자 이하여야 합니다."),
    NAME_PATTERN_INVALID(HttpStatus.BAD_REQUEST, "STUDY_005", "스터디 이름의 형식이 올바르지 않습니다."),

    // 스터디 기간
    PERIOD_NOT_NULL(HttpStatus.BAD_REQUEST, "STUDY_006", "시작일과 종료일은 필수입니다."),
    PERIOD_DATE_INVALID(HttpStatus.BAD_REQUEST, "STUDY_007", "종료일이 시작일보다 앞에 있을 수 없습니다."),
    PERIOD_DAYS_INVALID(HttpStatus.BAD_REQUEST, "STUDY_008", "기간은 %d일 이상 %d일 이하여야 합니다."),

    // 스터디 주제
    TOPIC_NOT_BLANK(HttpStatus.BAD_REQUEST, "STUDY_009", "스터디 주제는 필수입니다."),
    TOPIC_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "STUDY_010", "스터디 주제는 최대 %d자까지만 가능합니다."),

    // 스터디 권한
    UNAUTHORIZED_PROCESS_ACCESS(HttpStatus.FORBIDDEN, "STUDY_011", "프로세스 생성/수정/삭제 권한이 없습니다. 호스트만 가능합니다."),

    // 프로세스 기간
    PROCESS_PERIOD_DAYS_INVALID(HttpStatus.BAD_REQUEST, "STUDY_012", "프로세스 기간은 %d일 이상 %d일 이하여야 합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
