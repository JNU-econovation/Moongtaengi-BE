package econovation.moongtaengi.study.application.submission;

import java.util.List;

public record CreateSubmissionCommand(
        Long assignmentId,
        Long submitterId,
        String content,
        List<String> attachmentUrls
) {
}
