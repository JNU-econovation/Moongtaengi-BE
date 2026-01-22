package econovation.moongtaengi.study.application.assignment;

import econovation.moongtaengi.collection.domain.CollectionType;
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
        String fileUrl,
        String profileIconUrl
) {
    // JPQL 쿼리에서 CollectionType을 받아 String으로 변환하는 오버로드 생성자
    public AssignmentSummary(
            Long assignmentId,
            Long submissionId,
            Long memberId,
            String assignmentDescription,
            String nickname,
            AssignmentStatus status,
            Boolean isLate,
            String fileName,
            String fileUrl,
            CollectionType profileIcon
    ) {
        this(
                assignmentId,
                submissionId,
                memberId,
                assignmentDescription,
                nickname,
                status,
                isLate,
                fileName,
                fileUrl,
                profileIcon != null ? profileIcon.getUnlockedImageUrl() : ""
        );
    }

    public AssignmentSummary {
        assignmentDescription = (assignmentDescription == null) ? "" : assignmentDescription;
        nickname = (nickname == null) ? "" : nickname;
        isLate = (isLate != null) && isLate;
        fileName = (fileName == null) ? "" : fileName;
        fileUrl = (fileUrl == null) ? "" : fileUrl;
        profileIconUrl = (profileIconUrl == null) ? "" : profileIconUrl;
    }
}
