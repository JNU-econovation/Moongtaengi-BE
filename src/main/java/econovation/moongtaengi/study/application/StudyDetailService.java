package econovation.moongtaengi.study.application;

import econovation.moongtaengi.study.api.dto.StudyDetailResponse;
import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.StudyMember;
import econovation.moongtaengi.study.domain.StudyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyDetailService {
    private final StudyMemberRepository studyMemberRepository;

    public StudyDetailResponse getStudyDetail(Long studyId, Long memberId) {
        StudyMember studyMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.NOT_STUDY_MEMBER));

        return StudyDetailResponse.of(studyMember.getStudy(), studyMember);
    }
}
