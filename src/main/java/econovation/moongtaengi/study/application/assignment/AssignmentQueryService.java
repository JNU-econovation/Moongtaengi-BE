package econovation.moongtaengi.study.application.assignment;

import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.process.StudyProcessRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AssignmentQueryService {
    private final AssignmentRepository assignmentRepository;
    private final StudyProcessRepository processRepository;
    private final StudyMemberRepository studyMemberRepository;

    public List<AssignmentSummary> getAssignmentSummaries(Long memberId, Long processId) {
        Long studyId = processRepository.findStudyIdById(processId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.PROCESS_NOT_FOUND));

        boolean isMember = studyMemberRepository.existsByStudyIdAndMemberId(studyId, memberId);
        if (!isMember) {
            throw new StudyException(StudyErrorCode.NOT_STUDY_MEMBER);
        }

        return assignmentRepository.findSummaryByProcessIdAndStudyId(processId, studyId);
    }

}
