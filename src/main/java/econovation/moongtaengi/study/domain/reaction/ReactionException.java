package econovation.moongtaengi.study.domain.reaction;

import econovation.moongtaengi.global.exception.BusinessException;

public class ReactionException extends BusinessException {

    public ReactionException(ReactionErrorCode errorCode) {
        super(errorCode);
    }
}
