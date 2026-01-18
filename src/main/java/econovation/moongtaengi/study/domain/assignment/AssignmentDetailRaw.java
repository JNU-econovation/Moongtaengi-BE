package econovation.moongtaengi.study.domain.assignment;

import java.time.LocalDateTime;

public record AssignmentDetailRaw(
        String studyName,
        String assignmentContent,
        String nickname,
        Long assigneeId,
        int totalExperience,
        Long submissionId,
        LocalDateTime submitTime
) {
}
