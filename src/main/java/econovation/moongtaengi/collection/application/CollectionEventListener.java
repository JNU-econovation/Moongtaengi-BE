package econovation.moongtaengi.collection.application;

import econovation.moongtaengi.collection.domain.CollectionType;
import econovation.moongtaengi.collection.domain.event.CollectionUnlockedEvent;
import econovation.moongtaengi.member.domain.event.ExperienceAddedEvent;
import econovation.moongtaengi.member.domain.event.MemberRegisteredEvent;
import econovation.moongtaengi.onboarding.domain.event.OnboardingMissionCompletedEvent;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyRepository;
import econovation.moongtaengi.study.domain.event.StudyJoinedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 컬렉션 이벤트 리스너
 * 다양한 이벤트를 수신하여 컬렉션 해금 조건을 체크하고 자동으로 해금
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CollectionEventListener {

    private final CollectionService collectionService;
    private final StudyRepository studyRepository;

    /**
     * 회원 등록 완료 이벤트 처리
     * DEFAULT 컬렉션 자동 해금
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMemberRegistered(MemberRegisteredEvent event) {
        log.info("회원 등록 완료 이벤트 수신 - memberId: {}", event.memberId());

        // DEFAULT 컬렉션 자동 해금
        collectionService.unlockCollection(event.memberId(), CollectionType.DEFAULT);
        log.info("회원 {}에게 DEFAULT 컬렉션이 해금되었습니다", event.memberId());
    }

    /**
     * 경험치 획득 이벤트 처리
     * PRO(100 XP), MASTER(300 XP) 컬렉션 해금 조건 체크
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleExperienceAdded(ExperienceAddedEvent event) {
        log.info("경험치 획득 이벤트 수신 - memberId: {}, 획득 경험치: {}, 총 경험치: {}",
                event.memberId(), event.addedExperience(), event.totalExperience());

        int totalExp = event.totalExperience();

        // PRO 컬렉션 해금 (100 XP)
        if (totalExp >= 100) {
            collectionService.unlockCollection(event.memberId(), CollectionType.PRO);
        }

        // MASTER 컬렉션 해금 (300 XP)
        if (totalExp >= 300) {
            collectionService.unlockCollection(event.memberId(), CollectionType.MASTER);
        }
    }

    /**
     * 스터디 참여 이벤트 처리
     * BUNCH 컬렉션 해금 조건 체크 (4인 이상 스터디)
     * 특별 코드는 JoinStudyService에서 직접 처리
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleStudyJoined(StudyJoinedEvent event) {
        log.info("스터디 참여 이벤트 수신 - studyId: {}, memberId: {}",
                event.studyId(), event.memberId());

        // BUNCH 컬렉션 해금 조건 체크: 4인 이상 스터디 참여
        Study study = studyRepository.findByIdWithMembers(event.studyId())
                .orElseThrow(() -> new IllegalStateException("스터디를 찾을 수 없습니다: " + event.studyId()));

        int memberCount = study.getMembers().size();
        if (memberCount >= 4) {
            collectionService.unlockCollection(event.memberId(), CollectionType.BUNCH);
            log.info("회원 {}에게 뭉탱이뭉치 컬렉션이 해금되었습니다 (스터디 인원: {}명)",
                    event.memberId(), memberCount);
        }
    }

    /**
     * 컬렉션 해금 이벤트 처리
     * TREASURE_BAG 컬렉션 해금 조건 체크 (7개 이상 컬렉션 보유)
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCollectionUnlocked(CollectionUnlockedEvent event) {
        log.info("컬렉션 해금 이벤트 수신 - memberId: {}, collectionType: {}",
                event.memberId(), event.collectionType());

        // TREASURE_BAG 자기 자신 해금 방지
        if (event.collectionType() == CollectionType.TREASURE) {
            return;
        }

        // 현재 보유한 컬렉션 개수 조회 (방금 해금한 것 포함)
        long collectionCount = collectionService.getCollectionCount(event.memberId());

        // 7개 이상이면 TREASURE_BAG 해금
        if (collectionCount >= 7) {
            collectionService.unlockCollection(event.memberId(), CollectionType.TREASURE);
            log.info("회원 {}에게 TREASURE_BAG 컬렉션이 해금되었습니다 (총 컬렉션: {}개)",
                    event.memberId(), collectionCount);
        }
    }

    /**
     * 온보딩 미션 전체 완료 이벤트 처리
     * WOOD 컬렉션 해금 (보상)
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleOnboardingMissionCompleted(OnboardingMissionCompletedEvent event) {
        log.info("온보딩 미션 전체 완료 이벤트 수신 - memberId: {}", event.memberId());

        // WOOD 컬렉션 해금 (온보딩 완료 보상)
        collectionService.unlockCollection(event.memberId(), CollectionType.WOOD);
        log.info("회원 {}에게 나무곡괭이 뭉탱이 컬렉션이 해금되었습니다 (온보딩 완료 보상)",
                event.memberId());
    }
}
