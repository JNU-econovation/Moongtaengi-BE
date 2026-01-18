package econovation.moongtaengi.study.application;

import econovation.moongtaengi.collection.application.CollectionService;
import econovation.moongtaengi.collection.domain.CollectionType;
import econovation.moongtaengi.study.domain.InviteCode;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.StudyJoinValidator;
import econovation.moongtaengi.study.domain.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinStudyService {
    private final StudyRepository studyRepository;
    private final StudyJoinValidator studyJoinValidator;
    private final CollectionService collectionService;

    // 특별 코드 상수
    private static final String ECONO_CODE = "ECONOXXX";
    private static final String SPECIAL_CODE = "SPECIALXX";

    @Transactional
    public void joinStudy(Long memberId, String rawCode) {
        if (rawCode.equals(ECONO_CODE) || rawCode.equals(SPECIAL_CODE)) {
            // 특별 코드 체크 및 컬렉션 해금
            checkAndUnlockSpecialCollection(memberId, rawCode);
            return;
        }

        InviteCode inviteCode = new InviteCode(rawCode);

        Study study = studyRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        studyJoinValidator.validate(memberId, study);

        study.addGuest(memberId);
        studyRepository.save(study); // 도메인 이벤트 발행을 위해 명시적 저장 필요

        log.info("스터디 참가 완료 - memberId: {}, studyId: {}, studyName: {}",
                memberId, study.getId(), study.getName().getValue());
    }

    /**
     * 특별 코드 체크 및 컬렉션 해금
     * ECONO_CODE → ECONO_SPECIAL 컬렉션
     * SPECIAL_CODE → SPECIAL 컬렉션
     */
    private void checkAndUnlockSpecialCollection(Long memberId, String inviteCode) {
        if (ECONO_CODE.equals(inviteCode)) {
            collectionService.unlockCollection(memberId, CollectionType.ECONO_SPECIAL);
            log.info("회원 {}에게 특별 코드로 ECONO_SPECIAL 컬렉션이 해금되었습니다", memberId);
        } else if (SPECIAL_CODE.equals(inviteCode)) {
            collectionService.unlockCollection(memberId, CollectionType.SPECIAL);
            log.info("회원 {}에게 특별 코드로 SPECIAL 컬렉션이 해금되었습니다", memberId);
        }
    }
}
