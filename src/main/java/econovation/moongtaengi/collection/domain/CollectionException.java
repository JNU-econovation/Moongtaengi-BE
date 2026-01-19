package econovation.moongtaengi.collection.domain;

import econovation.moongtaengi.global.exception.BusinessException;
import econovation.moongtaengi.global.exception.ErrorCode;

public class CollectionException extends BusinessException{
    public CollectionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CollectionException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
