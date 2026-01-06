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
   
    //스터디 참여 중복
    ALREADY_JOINED_MEMBER(HttpStatus.CONFLICT, "STUDY_012", "이미 참여 중인 스터디입니다."),

    //임시회원 생성 제한
    STUDY_CREATION_DENIED_TEMP_MEMBER(HttpStatus.FORBIDDEN, "STUDY_013", "임시회원은 스터디를 생성할 수 없습니다."),

    //스터디 멤버
    CREATOR_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDY_014", "스터디 개설자 정보를 찾을 수 없습니다."),
    NOT_STUDY_MEMBER(HttpStatus.FORBIDDEN, "STUDY_018", "스터디 멤버가 아닙니다."),
  
    // 프로세스 기간
    PROCESS_PERIOD_DAYS_INVALID(HttpStatus.BAD_REQUEST, "STUDY_015", "프로세스 기간은 %d일 이상 %d일 이하여야 합니다."),
    PROCESS_DATE_OVERLAP(HttpStatus.BAD_REQUEST, "STUDY_020", "프로세스 날짜가 겹칩니다."),

    // 프로세스 메모
    MEMO_NOT_NULL(HttpStatus.BAD_REQUEST, "STUDY_016", "메모는 null일 수 없습니다."),
    MEMO_LENGTH_EXCEEDED(HttpStatus.BAD_REQUEST, "STUDY_017", "메모는 최대 %d자까지 입력 가능합니다."),

    // 프로세스
    PROCESS_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDY_019", "존재하지 않는 프로세스입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
