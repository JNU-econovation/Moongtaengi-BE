package econovation.moongtaengi.member.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import econovation.moongtaengi.global.exception.ErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NicknameCheckResponse(
        String nickname,
        boolean isAvailable,
        String code,
        String message
) {
    public static NicknameCheckResponse available(String nickname) {
        return new NicknameCheckResponse(nickname, true, null,"사용 가능한 닉네임입니다.");
    }


    public static NicknameCheckResponse unavailable(String nickname, ErrorCode errorCode) {
        return new NicknameCheckResponse(nickname, false, errorCode.getCode(), errorCode.getMessage());
    }
}
