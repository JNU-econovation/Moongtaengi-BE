package econovation.moongtaengi.study.application.submission;

import org.springframework.util.StringUtils;

public record CreateSubmissionCommand(
        Long assignmentId,
        Long submitterId,
        String content,
        String fileName,
        String fileUrl
) {
    public boolean hasAttachment() {
        return StringUtils.hasText(fileName) && StringUtils.hasText(fileUrl);
    }
}
