package econovation.moongtaengi.study.domain.submission;

import org.springframework.stereotype.Component;

@Component
public class SubmissionAuthorityValidator {

    public void validate(Long submitterId, Long assigneeId) {
        if (!submitterId.equals(assigneeId)) {
            throw new SubmissionException(SubmissionErrorCode.NOT_ASSIGNEE);
        }
    }
}
