package econovation.moongtaengi.study.domain.comment;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    INVALID_COMMENT_INFO(HttpStatus.BAD_REQUEST, "CM_001", "댓글의 필수 정보가 누락되었습니다."),
    CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "CM_002", "댓글은 %d자를 초과할 수 없습니다."),
    NOT_COMMENT_OWNER(HttpStatus.FORBIDDEN, "CM_003", "해당 댓글에 대한 편집 권한이 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
