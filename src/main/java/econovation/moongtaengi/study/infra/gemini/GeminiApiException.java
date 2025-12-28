package econovation.moongtaengi.study.infra.gemini;

import econovation.moongtaengi.global.exception.BusinessException;
import econovation.moongtaengi.global.exception.ErrorCode;

public class GeminiApiException extends BusinessException {
    public GeminiApiException(ErrorCode errorCode) {
        super(errorCode);
    }

    public GeminiApiException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public GeminiApiException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
