package econovation.moongtaengi.study.application;

import econovation.moongtaengi.collection.application.CollectionService;
import econovation.moongtaengi.collection.domain.CollectionType;
import econovation.moongtaengi.study.api.dto.StudyJoinResponse;
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
    public StudyJoinResponse joinStudy(Long memberId, String rawCode) {
        // ECONO 특별 코드
        if (rawCode.equals(ECONO_CODE)) {
            collectionService.unlockCollection(memberId, CollectionType.ECONO);
            log.info("회원 {}에게 특별 코드로 ECONO_SPECIAL 컬렉션이 해금되었습니다", memberId);
            return StudyJoinResponse.econo("✨ 히든 코드를 발견했습니다! 특별 컬렉션이 해금되었습니다.");
        }

        // SPECIAL 특별 코드
        if (rawCode.equals(SPECIAL_CODE)) {
            collectionService.unlockCollection(memberId, CollectionType.SPECIAL);
            log.info("회원 {}에게 특별 코드로 SPECIAL 컬렉션이 해금되었습니다", memberId);
            return StudyJoinResponse.special("✨ 히든 코드를 발견했습니다! 특별 컬렉션이 해금되었습니다.");
        }

        // 일반 스터디 참여
        InviteCode inviteCode = new InviteCode(rawCode);

        Study study = studyRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        studyJoinValidator.validate(memberId, study);

        study.addGuest(memberId);
        studyRepository.save(study); // 도메인 이벤트 발행을 위해 명시적 저장 필요

        log.info("스터디 참가 완료 - memberId: {}, studyId: {}, studyName: {}",
                memberId, study.getId(), study.getName().getValue());

        return StudyJoinResponse.study("스터디 가입이 완료되었습니다.");
    }

}
