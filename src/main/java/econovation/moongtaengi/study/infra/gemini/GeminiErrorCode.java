package econovation.moongtaengi.study.infra.gemini;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GeminiErrorCode implements ErrorCode {
    // API 호출 자체가 실패했거나 재시도 횟수를 초과했을 때
    GEMINI_GENERATION_FAILED(HttpStatus.BAD_GATEWAY, "GEMINI_001", "Gemini API 호출에 실패했습니다."),

    // JSON 응답 형식이 안 맞거나 파싱이 불가능할 때
    GEMINI_RESPONSE_PARSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "GEMINI_002", "Gemini 응답 형식을 파싱할 수 없습니다."),

    // 파싱은 됐으나 데이터가 비즈니스 로직(날짜 등)에 맞지 않을 때
    GEMINI_RESPONSE_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "GEMINI_003", "Gemini 응답 데이터가 유효하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
