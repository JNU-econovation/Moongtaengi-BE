package econovation.moongtaengi.study.infra.s3.domain;

import econovation.moongtaengi.global.exception.BusinessException;
import econovation.moongtaengi.global.exception.ErrorCode;

/**
 * S3 관련 예외
 */
public class S3Exception extends BusinessException {
    public S3Exception(ErrorCode errorCode) {
        super(errorCode);
    }

    public S3Exception(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
