package econovation.moongtaengi.study.application.assignment;

import econovation.moongtaengi.study.domain.assignment.AssignmentStatus;

public record AssignmentSummary(
        Long assignmentId,
        Long submissionId,
        Long memberId,
        String assignmentContent,
        String nickname,
        AssignmentStatus status,
        Boolean isLate,
        String fileUrl
) {

}
