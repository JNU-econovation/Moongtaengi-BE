package econovation.moongtaengi.study.domain.assignment;

import econovation.moongtaengi.global.exception.BusinessException;
import econovation.moongtaengi.global.exception.ErrorCode;

public class AssignmentException extends BusinessException {

    public AssignmentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
