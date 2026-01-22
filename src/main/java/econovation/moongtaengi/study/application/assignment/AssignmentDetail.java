package econovation.moongtaengi.study.application.assignment;

import econovation.moongtaengi.collection.domain.CollectionType;
import econovation.moongtaengi.member.domain.Title;
import econovation.moongtaengi.study.domain.assignment.AssignmentDetailRaw;
import econovation.moongtaengi.study.domain.reaction.ReactionStat;
import java.time.LocalDateTime;
import java.util.List;

public record AssignmentDetail(
        Long studyId,
        String studyName,
        String assignmentDescription,
        String nickname,
        String memberTitle,
        Long submissionId,
        LocalDateTime submitTime,
        boolean isOwner,
        String submissionContent,
        String submissionFileName,
        String submissionFileUrl,
        String profileIcon,
        List<ReactionStat> reactions
) {
    public static AssignmentDetail of(Long memberId, AssignmentDetailRaw raw, List<ReactionStat> reactions) {
        return new AssignmentDetail(
                raw.studyId(),
                nullToEmpty(raw.studyName()),
                nullToEmpty(raw.assignmentDescription()),
                nullToEmpty(raw.nickname()),
                Title.fromExperience(raw.totalExperience()).getDisplayName(),
                raw.submissionId(),
                raw.submitTime(),
                raw.assigneeId().equals(memberId),
                nullToEmpty(raw.submissionContent()),
                nullToEmpty(raw.submissionFileName()),
                nullToEmpty(raw.submissionFileUrl()),
                getProfileIconUrl(raw.profileIcon()),
                reactions != null ? reactions : List.of()
        );

    }

    private static String getProfileIconUrl(CollectionType type) {
        if (type == null) {
            return CollectionType.DEFAULT.getUnlockedImageUrl();
        }
        return type.getUnlockedImageUrl();
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
