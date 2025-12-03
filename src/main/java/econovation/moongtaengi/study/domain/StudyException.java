package econovation.moongtaengi.study.domain;

import econovation.moongtaengi.global.exception.BusinessException;
import econovation.moongtaengi.global.exception.ErrorCode;

public class StudyException extends BusinessException {
    public StudyException(ErrorCode errorCode) {
        super(errorCode);
    }

    public StudyException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
