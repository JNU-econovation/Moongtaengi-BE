package econovation.moongtaengi.study.api.dto;

import econovation.moongtaengi.study.application.submission.UpdateSubmissionCommand;

public record SubmissionUpdateRequest(
        String content,
        String fileName,
        String fileUrl
) {
    public UpdateSubmissionCommand toCommand(Long submissionId, Long requesterId) {
        return new UpdateSubmissionCommand(
                submissionId,
                requesterId,
                this.content,
                this.fileName,
                this.fileUrl
        );
    }
}
