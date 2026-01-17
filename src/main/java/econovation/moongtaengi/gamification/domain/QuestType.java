package econovation.moongtaengi.gamification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestType {
    COMMENT("댓글 작성", 2, 5, true),
    SUBMIT_ASSIGNMENT("과제 제출", 10, 1, true),
    REACTION("감정 표현", 1, 5, true),
    CREATE_STUDY("스터디 생성", 20, 1, false),
    LOGIN("로그인", 1, 1, true);

    private final String displayName;
    private final int experiencePoint;
    private final int dailyLimit;
    private final boolean isDaily;

    public boolean canComplete(int currentCount) {
        if (!isDaily) {
            return currentCount == 0;
        }
        return currentCount < dailyLimit;
    }
}