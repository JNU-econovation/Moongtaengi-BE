package econovation.moongtaengi.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 카카오 OAuth 관련
    KAKAO_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "KAKAO_001", "카카오 인증에 실패했습니다."),
    KAKAO_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "KAKAO_002", "카카오 서버에 오류가 발생했습니다."),

    // 회원 관련
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "존재하지 않는 회원입니다."),
    ALREADY_REGISTERED(HttpStatus.CONFLICT, "MEMBER_002", "이미 회원가입이 완료된 회원입니다."),

    // JWT 인증
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "만료된 토큰입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_003", "인증이 필요합니다."),

    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_002", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
