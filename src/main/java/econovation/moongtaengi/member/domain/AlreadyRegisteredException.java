package econovation.moongtaengi.member.domain;

import econovation.moongtaengi.global.exception.BusinessException;

public class AlreadyRegisteredException extends BusinessException {

    public AlreadyRegisteredException() {
        super(MemberErrorCode.ALREADY_REGISTERED);
    }
}
