package econovation.moongtaengi.global.exception;

public class KakaoServerException extends BusinessException {

    public KakaoServerException() {
            super(ErrorCode.KAKAO_SERVER_ERROR);
        }

    public KakaoServerException(Throwable cause) {
            super(ErrorCode.KAKAO_SERVER_ERROR, cause);
        }
    }
