package econovation.moongtaengi.study.domain.submission;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
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
import java.util.Optional;
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

    @Column(name = "assignment_id", nullable = false)
    private Long assignmentId;

    @Column(name = "submitter_id", nullable = false)
    private Long submitterId;

    @Embedded
    private SubmissionContent content;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "file_name", nullable = true)),
            @AttributeOverride(name = "url", column = @Column(name = "file_url", nullable = true))
    })
    @Getter(AccessLevel.NONE)
    private SubmissionAttachment attachment;

    @Column(name = "is_late", nullable = false)
    private boolean isLate;

    @Builder
    public static Submission create(
            Long assignmentId,
            Long submitterId,
            SubmissionContent content,
            LocalDateTime currentDateTime,
            LocalDateTime assignmentDeadline,
            SubmissionAttachment attachment) {
        validate(assignmentId, submitterId, content, currentDateTime, assignmentDeadline);

        boolean isLate = currentDateTime.isAfter(assignmentDeadline);

        Submission submission = new Submission(assignmentId, submitterId, content, attachment, isLate);

        submission.registerEvent(new SubmissionCreatedEvent(assignmentId, submitterId, isLate));

        return submission;
    }

    public Optional<SubmissionAttachment> getAttachment() {
        return Optional.ofNullable(attachment);
    }

    private static void validate(Long assignmentId, Long submitterId, SubmissionContent content,
            LocalDateTime currentDateTime, LocalDateTime assignmentDeadline) {
        if (assignmentId == null || submitterId == null || content == null ||
                currentDateTime == null || assignmentDeadline == null) {
            throw new SubmissionException(SubmissionErrorCode.CREATE_ARGUMENT_MISSING);
        }
    }
}
