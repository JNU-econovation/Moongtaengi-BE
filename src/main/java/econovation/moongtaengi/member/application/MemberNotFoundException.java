package econovation.moongtaengi.member.application;

import econovation.moongtaengi.global.exception.BusinessException;
import econovation.moongtaengi.member.domain.MemberErrorCode;

public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException() {
        super(MemberErrorCode.MEMBER_NOT_FOUND);
    }
}
