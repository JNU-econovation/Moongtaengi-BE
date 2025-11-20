package econovation.moongtaengi.member.api.dto;

public record NicknameCheckResponse(
        String nickname,
        boolean isAvailable,
        String message
        //에러코드 추가 예정
) {
    public static NicknameCheckResponse available(String nickname) {
        return new NicknameCheckResponse(nickname, true, "사용 가능한 닉네임입니다.");
    }


    public static NicknameCheckResponse unavailable(String nickname, String reason) {
        return new NicknameCheckResponse(nickname, false, reason);
    }
}
