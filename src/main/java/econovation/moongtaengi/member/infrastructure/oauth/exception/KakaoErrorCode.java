package econovation.moongtaengi.member.infrastructure.oauth.exception;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum KakaoErrorCode implements ErrorCode {
    KAKAO_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "KAKAO_001", "카카오 인증에 실패했습니다."),
    KAKAO_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "KAKAO_002", "카카오 서버에 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
