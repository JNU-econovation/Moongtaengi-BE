package econovation.moongtaengi.study.application.assignment;

import econovation.moongtaengi.study.domain.assignment.AssignmentStatus;

public record AssignmentSummary(
        Long assignmentId,
        Long submissionId,
        Long memberId,
        String assignmentDescription,
        String nickname,
        AssignmentStatus status,
        Boolean isLate,
        String fileName,
        String fileUrl
) {
    public AssignmentSummary {
        assignmentContent = (assignmentContent == null) ? "" : assignmentContent;
        nickname = (nickname == null) ? "" : nickname;
        isLate = (isLate != null) && isLate;
        fileName = (fileName == null) ? "" : fileName;
        fileUrl = (fileUrl == null) ? "" : fileUrl;
    }
}
