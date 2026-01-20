package econovation.moongtaengi.notification.api.dto;

import econovation.moongtaengi.onboarding.domain.OnboardingMission;
import econovation.moongtaengi.onboarding.domain.OnboardingMissionStatus;
import econovation.moongtaengi.onboarding.domain.OnboardingMissionType;
import lombok.Builder;

@Builder
public record OnboardingMissionResponse(
        String message,
        int order,
        int totalMissions,
        OnboardingMissionStatus status
) {
    public static OnboardingMissionResponse from(OnboardingMission mission) {
        OnboardingMissionType type = mission.getCurrentMission();
        return OnboardingMissionResponse.builder()
                .message(type.getDescription())
                .order(type.getOrder())
                .totalMissions(OnboardingMissionType.getTotalMissions())
                .status(mission.getStatus())
                .build();
    }
}
