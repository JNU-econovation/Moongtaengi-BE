package econovation.moongtaengi.study.domain.submission;

public record SubmissionCreatedEvent(
        Long assignmentId,
        boolean isLate
) {
}
