package econovation.moongtaengi.onboarding.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OnboardingMissionRepository extends JpaRepository<OnboardingMission, Long> {

    Optional<OnboardingMission> findByMemberId(Long memberId);
}
