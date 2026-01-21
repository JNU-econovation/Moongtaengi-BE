package econovation.moongtaengi.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 알림 종류
 */
@Getter
@RequiredArgsConstructor
public enum NotificationType {
    ASSIGNMENT_SUBMITTED("과제 제출됨"),
    DAILY_QUEST_RESET("일일 퀘스트 초기화"),
    ASSIGNMENT_OVERDUE("과제 미제출"),
    ASSIGNMENT_DEADLINE_SOON("마감 임박"),
    COMMENT_RECEIVED("댓글 받음");
    // 추후: ASSIGNMENT_APPROVED

    private final String description;
}
