package econovation.moongtaengi.gamification.domain;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "daily_quests", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "quest_type"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyQuest extends BaseEntity {

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "quest_type", nullable = false)
    private QuestType questType;

    @Column(nullable = false)
    private int completedCount = 0;

    @Column(nullable = false)
    private LocalDate lastCompletedDate;

    @Builder
    private DailyQuest(Long memberId, QuestType questType, int completedCount, LocalDate lastCompletedDate) {
        this.memberId = memberId;
        this.questType = questType;
        this.completedCount = completedCount;
        this.lastCompletedDate = lastCompletedDate;
    }

    public static DailyQuest create(Long memberId, QuestType questType) {
        return DailyQuest.builder()
                .memberId(memberId)
                .questType(questType)
                .completedCount(0)
                .lastCompletedDate(LocalDate.now())
                .build();
    }

    public boolean canComplete() {
        resetIfNewDay();
        return questType.canComplete(completedCount);
    }

    public void complete() {
        if (!canComplete()) {
            throw new IllegalStateException("일일 퀘스트 제한을 초과했습니다.");
        }
        this.completedCount++;
        this.lastCompletedDate = LocalDate.now();
    }

    private void resetIfNewDay() {
        LocalDate today = LocalDate.now();
        if (!lastCompletedDate.equals(today) && questType.isDaily()) {
            this.completedCount = 0;
            this.lastCompletedDate = today;
        }
    }

    public boolean isCompletedToday() {
        return lastCompletedDate.equals(LocalDate.now()) && completedCount > 0;
    }
}