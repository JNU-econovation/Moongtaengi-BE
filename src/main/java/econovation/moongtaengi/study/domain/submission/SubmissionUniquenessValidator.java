package econovation.moongtaengi.study.domain.submission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubmissionUniquenessValidator {

    private final SubmissionRepository submissionRepository;

    public void validate(Long assignmentId) {
        boolean exists = submissionRepository.existsByAssignmentId(assignmentId);
        if (exists) {
            throw new SubmissionException(SubmissionErrorCode.ALREADY_SUBMITTED);
        }
    }
}
