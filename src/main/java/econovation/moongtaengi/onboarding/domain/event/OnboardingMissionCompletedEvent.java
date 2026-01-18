package econovation.moongtaengi.onboarding.domain.event;

/**
 * 온보딩 미션 전체 완료 이벤트
 * 모든 온보딩 미션을 완료했을 때 발행되어 보상 지급에 사용
 */
public record OnboardingMissionCompletedEvent(
        Long memberId
) {
}