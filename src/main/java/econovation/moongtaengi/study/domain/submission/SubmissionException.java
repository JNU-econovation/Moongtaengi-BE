package econovation.moongtaengi.study.domain.submission;

import econovation.moongtaengi.global.exception.BusinessException;

public class SubmissionException extends BusinessException {
    public SubmissionException(SubmissionErrorCode errorCode) {
        super(errorCode);
    }

    public SubmissionException(SubmissionErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}