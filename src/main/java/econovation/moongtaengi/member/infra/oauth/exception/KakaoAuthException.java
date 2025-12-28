package econovation.moongtaengi.member.infra.oauth.exception;

import econovation.moongtaengi.global.exception.BusinessException;

public class KakaoAuthException extends BusinessException {

    public KakaoAuthException() {
        super(KakaoErrorCode.KAKAO_AUTH_FAILED);
    }

    public KakaoAuthException(Throwable cause) {
        super(KakaoErrorCode.KAKAO_AUTH_FAILED, cause);
    }
}
