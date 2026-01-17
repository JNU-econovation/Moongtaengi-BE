package econovation.moongtaengi.study.domain.submission;

import java.time.LocalDateTime;

public interface AssignmentInfoProvider {

    AssignmentInfo getAssignmentInfo(Long assignmentId);

    record AssignmentInfo(
            Long assigneeId,
            LocalDateTime deadline
    ) {}
}
