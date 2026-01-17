package econovation.moongtaengi.notification.api.dto;

import econovation.moongtaengi.gamification.domain.DailyQuest;
import econovation.moongtaengi.gamification.domain.QuestType;
import lombok.Builder;

@Builder
public record DailyQuestResponse(
        QuestType type,
        int current,
        int max
) {
    public static DailyQuestResponse from(DailyQuest dailyQuest) {
        return DailyQuestResponse.builder()
                .type(dailyQuest.getQuestType())
                .current(dailyQuest.getCompletedCount())
                .max(dailyQuest.getQuestType().getDailyLimit())
                .build();
    }
}
