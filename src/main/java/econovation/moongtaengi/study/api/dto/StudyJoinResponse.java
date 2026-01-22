package econovation.moongtaengi.study.api.dto;

/**
 * 스터디 가입 응답 DTO
 * 특별 코드 타입(ECONO, SPECIAL)과 일반 스터디 참여(STUDY)를 구분하여 응답
 */
public record StudyJoinResponse(
        String codeType,
        String message
) {
    // 코드 타입 상수
    public static final String TYPE_STUDY = "STUDY";
    public static final String TYPE_ECONO = "ECONO";
    public static final String TYPE_SPECIAL = "SPECIAL";

    /**
     * ECONO 특별 코드로 컬렉션 해금한 경우
     */
    public static StudyJoinResponse econo(String message) {
        return new StudyJoinResponse(TYPE_ECONO, message);
    }

    /**
     * SPECIAL(Kane) 특별 코드로 컬렉션 해금한 경우
     */
    public static StudyJoinResponse special(String message) {
        return new StudyJoinResponse(TYPE_SPECIAL, message);
    }

    /**
     * 일반 스터디에 참여한 경우
     */
    public static StudyJoinResponse study(String message) {
        return new StudyJoinResponse(TYPE_STUDY, message);
    }
}
