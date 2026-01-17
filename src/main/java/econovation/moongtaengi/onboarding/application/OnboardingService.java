package econovation.moongtaengi.onboarding.application;

import econovation.moongtaengi.notification.api.dto.OnboardingMissionResponse;
import econovation.moongtaengi.onboarding.domain.OnboardingMission;
import econovation.moongtaengi.onboarding.domain.OnboardingMissionRepository;
import econovation.moongtaengi.onboarding.domain.OnboardingMissionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 온보딩 미션 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OnboardingService {

    private final OnboardingMissionRepository onboardingMissionRepository;

    /**
     * 회원의 현재 온보딩 미션 조회
     */
    public OnboardingMissionResponse getOnboardingMission(Long memberId) {
        OnboardingMission mission = onboardingMissionRepository.findByMemberId(memberId)
                .orElseGet(() -> onboardingMissionRepository.save(OnboardingMission.create(memberId)));
        return OnboardingMissionResponse.from(mission);
    }

    /**
     * 온보딩 미션 진행도 업데이트
     * 현재 미션이 targetMission과 일치하는 경우에만 다음 단계로 진행
     */
    @Transactional
    public void updateOnboardingMission(Long memberId, OnboardingMissionType targetMission) {
        try {
            Optional<OnboardingMission> missionOpt = onboardingMissionRepository.findByMemberId(memberId);

            if (missionOpt.isEmpty()) {
                // 미션이 없으면 새로 생성
                OnboardingMission newMission = OnboardingMission.create(memberId);
                onboardingMissionRepository.save(newMission);
                log.info("온보딩 미션 생성 - memberId: {}, currentMission: {}", memberId, newMission.getCurrentMission());
                missionOpt = onboardingMissionRepository.findByMemberId(memberId);
            }

            OnboardingMission mission = missionOpt.get();

            // 현재 미션이 목표 미션과 일치할 때만 진행
            if (mission.getCurrentMission() == targetMission) {
                if (!mission.isLastMission()) {
                    mission.completeCurrentAndMoveToNext();
                    onboardingMissionRepository.save(mission);
                    log.info("온보딩 미션 진행 - memberId: {}, completedMission: {}, nextMission: {}",
                            memberId, targetMission, mission.getCurrentMission());
                } else {
                    log.info("온보딩 미션 전체 완료 - memberId: {}", memberId);
                }
            }
        } catch (Exception e) {
            log.error("온보딩 미션 업데이트 실패 - memberId: {}, targetMission: {}, error: {}",
                    memberId, targetMission, e.getMessage());
        }
    }
}