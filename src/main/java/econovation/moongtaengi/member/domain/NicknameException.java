package econovation.moongtaengi.member.domain;

import econovation.moongtaengi.global.exception.BusinessException;

public class NicknameException extends BusinessException {
    public NicknameException(MemberErrorCode errorCode) {
        super(errorCode);
    }

    public NicknameException(MemberErrorCode errorCode, Object ...args) {
        super(errorCode, args);
    }
}
