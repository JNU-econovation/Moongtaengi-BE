package econovation.moongtaengi.onboarding.application;

import econovation.moongtaengi.notification.api.dto.OnboardingMissionResponse;
import econovation.moongtaengi.onboarding.domain.OnboardingMission;
import econovation.moongtaengi.onboarding.domain.OnboardingMissionRepository;
import econovation.moongtaengi.onboarding.domain.OnboardingMissionStatus;
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
     * 회원의 온보딩 미션 조회
     * 상태(WAITING, COMPLETED)와 무관하게 모든 미션을 반환
     *
     * @param memberId 회원 ID
     * @return 온보딩 미션 (항상 반환, null 아님)
     */
    @Transactional
    public OnboardingMissionResponse getOnboardingMission(Long memberId) {
        // existingMission: 상태와 무관하게 회원에게 할당된 미션이 존재하는지 확인
        // WAITING 또는 COMPLETED 상태 모두 반환
        OnboardingMission mission = onboardingMissionRepository.findByMemberId(memberId)
                .orElseGet(() -> {
                    // 미션이 없으면 새로 생성
                    OnboardingMission newMission = OnboardingMission.create(memberId);
                    onboardingMissionRepository.save(newMission);
                    log.info("새로운 온보딩 미션 생성 - memberId: {}", memberId);
                    return newMission;
                });

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
                    // 마지막 미션 완료
                    mission.markAsCompleted();
                    onboardingMissionRepository.save(mission);
                    log.info("온보딩 미션 전체 완료 - memberId: {}", memberId);
                }
            }
        } catch (Exception e) {
            log.error("온보딩 미션 업데이트 실패 - memberId: {}, targetMission: {}, error: {}",
                    memberId, targetMission, e.getMessage());
        }
    }
}
