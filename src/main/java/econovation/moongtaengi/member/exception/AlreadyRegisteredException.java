package econovation.moongtaengi.member.exception;

import econovation.moongtaengi.global.exception.BusinessException;
import econovation.moongtaengi.global.exception.ErrorCode;

public class AlreadyRegisteredException extends BusinessException {

    public AlreadyRegisteredException() {
        super(ErrorCode.ALREADY_REGISTERED);
    }
}
