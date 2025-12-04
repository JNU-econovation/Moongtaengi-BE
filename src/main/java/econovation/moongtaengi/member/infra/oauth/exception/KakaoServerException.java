package econovation.moongtaengi.member.infra.oauth.exception;

import econovation.moongtaengi.global.exception.BusinessException;

public class KakaoServerException extends BusinessException {

    public KakaoServerException() {
            super(KakaoErrorCode.KAKAO_SERVER_ERROR);
        }

    public KakaoServerException(Throwable cause) {
            super(KakaoErrorCode.KAKAO_SERVER_ERROR, cause);
        }
    }
