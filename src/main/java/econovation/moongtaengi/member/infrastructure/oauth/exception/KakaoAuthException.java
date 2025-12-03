package econovation.moongtaengi.member.infrastructure.oauth.exception;

import econovation.moongtaengi.global.exception.BusinessException;
import econovation.moongtaengi.global.exception.ErrorCode;

public class KakaoAuthException extends BusinessException {

    public KakaoAuthException() {
        super(KakaoErrorCode.KAKAO_AUTH_FAILED);
    }

    public KakaoAuthException(Throwable cause) {
        super(KakaoErrorCode.KAKAO_AUTH_FAILED, cause);
    }
}
