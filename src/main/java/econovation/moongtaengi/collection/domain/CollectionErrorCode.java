package econovation.moongtaengi.collection.domain;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 컬렉션 도메인 에러 코드
 */
@Getter
@RequiredArgsConstructor
public enum CollectionErrorCode implements ErrorCode {
    COLLECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "COLLECTION_001", "존재하지 않는 컬렉션입니다."),
    COLLECTION_NOT_OWNED(HttpStatus.FORBIDDEN, "COLLECTION_002", "보유하지 않은 컬렉션입니다."),
    COLLECTION_ALREADY_UNLOCKED(HttpStatus.CONFLICT, "COLLECTION_003", "이미 해금된 컬렉션입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}