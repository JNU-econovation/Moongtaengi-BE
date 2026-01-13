package econovation.moongtaengi.admin.domain;

import econovation.moongtaengi.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminErrorCode implements ErrorCode {
    // 인증
    ADMIN_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "ADMIN_001", "관리자 로그인에 실패했습니다."),
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN_002", "존재하지 않는 관리자입니다."),
    UNAUTHORIZED_ADMIN_ACCESS(HttpStatus.FORBIDDEN, "ADMIN_003", "관리자 권한이 필요합니다."),

    // 회원 관리
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN_004", "존재하지 않는 회원입니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "ADMIN_005", "이미 존재하는 카카오 ID입니다."),
    CANNOT_DELETE_ADMIN(HttpStatus.BAD_REQUEST, "ADMIN_006", "관리자 계정은 삭제할 수 없습니다."),

    // 스터디 관리
    STUDY_NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN_007", "존재하지 않는 스터디입니다."),
    CANNOT_DELETE_STUDY_WITH_MEMBERS(HttpStatus.BAD_REQUEST, "ADMIN_008", "멤버가 있는 스터디는 삭제할 수 없습니다."),
    HOST_NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN_009", "존재하지 않는 호스트입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
