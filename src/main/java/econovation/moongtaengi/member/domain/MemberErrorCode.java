package econovation.moongtaengi.member.domain;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    //멤버
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "존재하지 않는 회원입니다."),
    ALREADY_REGISTERED(HttpStatus.CONFLICT, "MEMBER_002", "이미 회원가입이 완료된 회원입니다."),

    //닉네임
    NICKNAME_NOT_BLANK(HttpStatus.BAD_REQUEST, "NICK_001", "닉네임은 비어있을 수 없습니다."),
    NICKNAME_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "NICK_002", "닉네임은 %d자 이상 %d자 이하여야 합니다."),
    NICKNAME_INVALID_PATTERN(HttpStatus.BAD_REQUEST, "NICK_003", "닉네임은 한글과 숫자 조합으로만 가능합니다."),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "NICK_004", "이미 사용 중인 닉네임입니다."),
    NICKNAME_BAD_WORD(HttpStatus.BAD_REQUEST, "NICK_005", "비속어가 포함되어 있습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
