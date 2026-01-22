package econovation.moongtaengi.study.application.submission;

public record UpdateSubmissionCommand(
        Long submissionId,
        Long requesterId,
        String content,
        String fileName,
        String fileUrl
) {

}
