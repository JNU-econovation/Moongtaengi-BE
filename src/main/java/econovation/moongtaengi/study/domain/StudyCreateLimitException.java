package econovation.moongtaengi.study.domain;

import econovation.moongtaengi.global.exception.BusinessException;

public class StudyCreateLimitException extends BusinessException {

    public StudyCreateLimitException(int limit) {
        super(StudyErrorCode.STUDY_CREATION_LIMIT_EXCEEDED, limit);
    }
}
