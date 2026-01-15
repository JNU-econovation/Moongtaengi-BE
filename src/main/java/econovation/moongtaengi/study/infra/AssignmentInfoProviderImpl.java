package econovation.moongtaengi.study.infra;

import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.submission.AssignmentInfoProvider;
import econovation.moongtaengi.study.domain.submission.SubmissionErrorCode;
import econovation.moongtaengi.study.domain.submission.SubmissionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignmentInfoProviderImpl implements AssignmentInfoProvider {

    private final AssignmentRepository assignmentRepository;

    @Override
    public AssignmentInfo getAssignmentInfo(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new SubmissionException(SubmissionErrorCode.INVALID_ASSIGNMENT_ID));

        return new AssignmentInfo(
                assignment.getAssigneeId(),
                assignment.getDeadline().getValue()
        );
    }
}
