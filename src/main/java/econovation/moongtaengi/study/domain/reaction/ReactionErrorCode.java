package econovation.moongtaengi.study.domain.reaction;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReactionErrorCode implements ErrorCode {
    INVALID_REACTION_INFO(HttpStatus.BAD_REQUEST, "RT_001", "감정표현의 필수 정보가 누락되었습니다."),
    SUBMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "RT_002", "존재하지 않는 제출물입니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "RT_003", "감정표현을 할 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
