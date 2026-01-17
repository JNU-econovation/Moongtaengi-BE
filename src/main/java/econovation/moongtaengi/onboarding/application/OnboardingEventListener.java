package econovation.moongtaengi.onboarding.application;

import econovation.moongtaengi.onboarding.domain.OnboardingMissionType;
import econovation.moongtaengi.study.domain.event.StudyJoinedEvent;
import econovation.moongtaengi.study.domain.submission.SubmissionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 온보딩 이벤트 리스너
 * - 메인 트랜잭션 커밋 후 새 트랜잭션에서 온보딩 미션 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OnboardingEventListener {

    private final OnboardingService onboardingService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleStudyJoined(StudyJoinedEvent event) {
        log.info("StudyJoinedEvent 수신 - studyId: {}, memberId: {}", event.studyId(), event.memberId());

        try {
            // 온보딩 미션 진행도 업데이트 (JOIN_STUDY)
            onboardingService.updateOnboardingMission(event.memberId(), OnboardingMissionType.JOIN_STUDY);
        } catch (Exception e) {
            log.error("스터디 참가 온보딩 미션 업데이트 실패 - studyId: {}, memberId: {}, error: {}",
                    event.studyId(), event.memberId(), e.getMessage());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleSubmissionCreated(SubmissionCreatedEvent event) {
        log.info("SubmissionCreatedEvent 수신 (온보딩) - assignmentId: {}, submitterId: {}",
                event.assignmentId(), event.submitterId());

        try {
            // 온보딩 미션 진행도 업데이트 (UPLOAD_ASSIGNMENT)
            onboardingService.updateOnboardingMission(event.submitterId(), OnboardingMissionType.UPLOAD_ASSIGNMENT);
        } catch (Exception e) {
            log.error("과제 제출 온보딩 미션 업데이트 실패 - submitterId: {}, error: {}",
                    event.submitterId(), e.getMessage());
        }
    }
}