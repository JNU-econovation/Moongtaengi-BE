package econovation.moongtaengi.member.infrastructure.oauth.exception;

import econovation.moongtaengi.global.exception.BusinessException;
import econovation.moongtaengi.global.exception.ErrorCode;

public class KakaoServerException extends BusinessException {

    public KakaoServerException() {
            super(KakaoErrorCode.KAKAO_SERVER_ERROR);
        }

    public KakaoServerException(Throwable cause) {
            super(KakaoErrorCode.KAKAO_SERVER_ERROR, cause);
        }
    }
