package econovation.moongtaengi.study.domain.submission;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "submissions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Submission extends BaseEntity {
    private static final int MAX_ATTACHMENTS_SIZE = 1;

    @Column(name = "assignment_id", nullable = false)
    private Long assignmentId;

    @Column(name = "submitter_id", nullable = false)
    private Long submitterId;

    @Embedded
    private SubmissionContent content;

    @ElementCollection
    @CollectionTable(
            name = "submission_attachments",
            joinColumns = @JoinColumn(name = "submission_id")
    )
    @AttributeOverride(
            name = "url",
            column = @Column(name = "file_url", nullable = false)
    )
    private List<SubmissionAttachment> attachments = new ArrayList<>();

    @Column(name = "is_late", nullable = false)
    private boolean isLate;

    @Builder
    public static Submission create(
            Long assignmentId,
            Long submitterId,
            SubmissionContent content,
            LocalDateTime currentDateTime,
            LocalDateTime assignmentDeadline,
            List<SubmissionAttachment> attachments) {
        validate(assignmentId, submitterId, content, currentDateTime, assignmentDeadline, attachments);

        boolean isLate = currentDateTime.isAfter(assignmentDeadline);

        List<SubmissionAttachment> safeAttachments = (attachments != null)
                ? new ArrayList<>(attachments)
                : new ArrayList<>();

        Submission submission = new Submission(assignmentId, submitterId, content, safeAttachments, isLate);

        submission.registerEvent(new SubmissionCreatedEvent(assignmentId, isLate));

        return submission;
    }

    private static void validate(Long assignmentId, Long submitterId, SubmissionContent content,
            LocalDateTime currentDateTime, LocalDateTime assignmentDeadline,
            List<SubmissionAttachment> attachments) {
        if (assignmentId == null || submitterId == null || content == null ||
                currentDateTime == null || assignmentDeadline == null) {
            throw new SubmissionException(SubmissionErrorCode.CREATE_ARGUMENT_MISSING);
        }

        if (attachments != null && attachments.size() > 1) {
            throw new SubmissionException(SubmissionErrorCode.ATTACHMENT_LIMIT_EXCEEDED, MAX_ATTACHMENTS_SIZE);
        }
    }
}
