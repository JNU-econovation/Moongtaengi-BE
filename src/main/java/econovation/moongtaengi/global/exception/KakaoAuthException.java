package econovation.moongtaengi.global.exception;

public class KakaoAuthException extends BusinessException  {

    public KakaoAuthException() {
        super(ErrorCode.KAKAO_AUTH_FAILED);
    }

    public KakaoAuthException(Throwable cause) {
        super(ErrorCode.KAKAO_AUTH_FAILED, cause);
    }
}
