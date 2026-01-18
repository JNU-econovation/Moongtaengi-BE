package econovation.moongtaengi.study.application.assignment;

import econovation.moongtaengi.member.domain.Title;
import econovation.moongtaengi.study.domain.assignment.AssignmentDetailRaw;
import java.time.LocalDateTime;

public record AssignmentDetail(
        Long studyId,
        String studyName,
        String assignmentContent,
        String nickname,
        String memberTitle,
        Long submissionId,
        LocalDateTime submitTime,
        boolean isOwner
) {
    public static AssignmentDetail of(Long memberId, AssignmentDetailRaw raw) {
        return new AssignmentDetail(
                raw.studyId(),
                raw.studyName(),
                raw.assignmentContent(),
                raw.nickname(),
                Title.fromExperience(raw.totalExperience()).getDisplayName(),
                raw.submissionId(),
                raw.submitTime(),
                raw.assigneeId().equals(memberId)
        );
    }
}
