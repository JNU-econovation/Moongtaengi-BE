package econovation.moongtaengi.study.infra.s3.exception;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode {
    // 파일 검증
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "S3_001", "지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "S3_002", "파일 크기가 제한을 초과했습니다. (최대 %dMB)"),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "S3_003", "파일 이름이 올바르지 않습니다."),

    // S3 업로드
    PRESIGNED_URL_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_004", "Presigned URL 생성에 실패했습니다."),
    S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_005", "S3 업로드에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
