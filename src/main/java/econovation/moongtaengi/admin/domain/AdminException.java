package econovation.moongtaengi.admin.domain;

import econovation.moongtaengi.global.exception.BusinessException;
import econovation.moongtaengi.global.exception.ErrorCode;

public class AdminException extends BusinessException {
    public AdminException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AdminException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
