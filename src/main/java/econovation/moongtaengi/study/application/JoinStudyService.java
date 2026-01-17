package econovation.moongtaengi.study.application;

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

    @Transactional
    public void joinStudy(Long memberId, String rawCode) {
        InviteCode inviteCode = new InviteCode(rawCode);

        Study study = studyRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        studyJoinValidator.validate(memberId, study);

        study.addGuest(memberId);
        studyRepository.save(study); // 도메인 이벤트 발행을 위해 명시적 저장 필요

        log.info("스터디 참가 완료 - memberId: {}, studyId: {}, studyName: {}",
                memberId, study.getId(), study.getName().getValue());
    }
}
