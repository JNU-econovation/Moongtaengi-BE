package econovation.moongtaengi.notification.api.dto;

import econovation.moongtaengi.onboarding.domain.OnboardingMission;
import econovation.moongtaengi.onboarding.domain.OnboardingMissionStatus;
import econovation.moongtaengi.onboarding.domain.OnboardingMissionType;
import lombok.Builder;

@Builder
public record OnboardingMissionResponse(
        OnboardingMissionType missionType,
        String message,
        int order,
        int totalMissions,
        OnboardingMissionStatus status
) {
    public static OnboardingMissionResponse from(OnboardingMission mission) {
        OnboardingMissionType type = mission.getCurrentMission();
        String message = String.format("[온보딩] %s (%d/%d)",
                type.getDescription(),
                type.getOrder(),
                OnboardingMissionType.getTotalMissions());

        return OnboardingMissionResponse.builder()
                .missionType(type)
                .message(message)
                .order(type.getOrder())
                .totalMissions(OnboardingMissionType.getTotalMissions())
                .status(mission.getStatus())
                .build();
    }
}