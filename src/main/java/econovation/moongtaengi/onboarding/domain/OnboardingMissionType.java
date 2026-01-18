package econovation.moongtaengi.onboarding.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 온보딩 미션 종류
 */
@Getter
@RequiredArgsConstructor
public enum OnboardingMissionType {
    JOIN_STUDY(1, "스터디 참가하기"),
    UPLOAD_ASSIGNMENT(2, "과제 업로드 하기"),
    WRITE_COMMENT(3, "다른 스터디원 과제에 댓글 작성하기"),
    CHANGE_COLLECTION(4, "컬렉션 바꿔보기");

    private final int order;
    private final String description;

    public static int getTotalMissions() {
        return values().length;
    }
}