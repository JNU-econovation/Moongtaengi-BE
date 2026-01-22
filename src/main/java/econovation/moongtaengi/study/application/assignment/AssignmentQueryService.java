package econovation.moongtaengi.study.application.assignment;

import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.assignment.AssignmentDetailRaw;
import econovation.moongtaengi.study.domain.assignment.AssignmentException;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.process.StudyProcessRepository;
import econovation.moongtaengi.study.domain.assignment.AssignmentErrorCode;
import econovation.moongtaengi.study.domain.reaction.ReactionRepository;
import econovation.moongtaengi.study.domain.reaction.ReactionStat;
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
    private final ReactionRepository reactionRepository;

    public List<AssignmentSummary> getAssignmentSummaries(Long memberId, Long processId) {
        Long studyId = processRepository.findStudyIdById(processId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.PROCESS_NOT_FOUND));

        boolean isMember = studyMemberRepository.existsByStudyIdAndMemberId(studyId, memberId);
        if (!isMember) {
            throw new StudyException(StudyErrorCode.NOT_STUDY_MEMBER);
        }

        return assignmentRepository.findSummaryByProcessIdAndStudyId(processId, studyId);
    }

    public AssignmentDetail getAssignmentDetail(Long memberId, Long assignmentId) {
        AssignmentDetailRaw raw = assignmentRepository.findDetailRawById(assignmentId)
                .orElseThrow(() -> new AssignmentException(AssignmentErrorCode.ASSIGNMENT_NOT_FOUND));

        boolean isMember = studyMemberRepository.existsByStudyIdAndMemberId(raw.studyId(), memberId);
        if (!isMember) {
            throw new StudyException(StudyErrorCode.NOT_STUDY_MEMBER);
        }

        List<ReactionStat> reactions = List.of();
        if (raw.submissionId() != null) {
            reactions = reactionRepository.findStatBySubmissionIdAndMemberId(raw.submissionId(), memberId);
        }


        return AssignmentDetail.of(memberId, raw, reactions);
    }

}
