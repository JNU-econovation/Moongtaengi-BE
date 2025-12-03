package econovation.moongtaengi.member.domain;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "존재하지 않는 회원입니다."),
    ALREADY_REGISTERED(HttpStatus.CONFLICT, "MEMBER_002", "이미 회원가입이 완료된 회원입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
