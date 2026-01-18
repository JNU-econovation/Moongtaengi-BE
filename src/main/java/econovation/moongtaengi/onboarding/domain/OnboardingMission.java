package econovation.moongtaengi.onboarding.domain;

import econovation.moongtaengi.global.entity.BaseEntity;
import econovation.moongtaengi.onboarding.domain.event.OnboardingMissionCompletedEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 온보딩 미션 진행 상태
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnboardingMission extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OnboardingMissionType currentMission;

    private OnboardingMission(Long memberId, OnboardingMissionType currentMission) {
        this.memberId = memberId;
        this.currentMission = currentMission;
    }

    public static OnboardingMission create(Long memberId) {
        return new OnboardingMission(memberId, OnboardingMissionType.JOIN_STUDY);
    }

    public void completeCurrentAndMoveToNext() {
        OnboardingMissionType[] missions = OnboardingMissionType.values();
        for (int i = 0; i < missions.length - 1; i++) {
            if (missions[i] == this.currentMission) {
                this.currentMission = missions[i + 1];
                return;
            }
        }
    }

    public boolean isLastMission() {
        OnboardingMissionType[] missions = OnboardingMissionType.values();
        return this.currentMission == missions[missions.length - 1];
    }

    /**
     * 마지막 온보딩 미션 완료
     * 완료 이벤트를 발행하여 보상 지급
     */
    public void markAsCompleted() {
        registerEvent(new OnboardingMissionCompletedEvent(this.memberId));
    }
}