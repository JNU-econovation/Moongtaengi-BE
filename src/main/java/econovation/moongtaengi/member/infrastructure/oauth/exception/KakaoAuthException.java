package econovation.moongtaengi.member.infrastructure.oauth.exception;

import econovation.moongtaengi.global.exception.BusinessException;
import econovation.moongtaengi.global.exception.ErrorCode;

public class KakaoAuthException extends BusinessException {

    public KakaoAuthException() {
        super(ErrorCode.KAKAO_AUTH_FAILED);
    }

    public KakaoAuthException(Throwable cause) {
        super(ErrorCode.KAKAO_AUTH_FAILED, cause);
    }
}
