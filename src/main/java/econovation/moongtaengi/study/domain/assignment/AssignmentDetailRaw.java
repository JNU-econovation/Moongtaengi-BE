package econovation.moongtaengi.study.domain.assignment;

import econovation.moongtaengi.collection.domain.CollectionType;
import java.time.LocalDateTime;

public record AssignmentDetailRaw(
        Long studyId,
        String studyName,
        String assignmentDescription,
        String nickname,
        Long assigneeId,
        int totalExperience,
        Long submissionId,
        LocalDateTime submitTime,
        String submissionContent,
        String submissionFileName,
        String submissionFileUrl,
        CollectionType profileIcon
) {
}
