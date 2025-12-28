package econovation.moongtaengi.global.security.exception;

import econovation.moongtaengi.global.exception.BusinessException;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException() {
        super(AuthErrorCode.UNAUTHORIZED);
    }
}
