package econovation.moongtaengi.study.api.dto;

import econovation.moongtaengi.study.application.submission.CreateSubmissionCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SubmissionCreateRequest(
        @NotNull
        Long assignmentId,

        @NotBlank
        String content,

        List<String> attachmentUrls
) {
    public CreateSubmissionCommand toCommand(Long submitterId) {
        return new CreateSubmissionCommand(
                assignmentId,
                submitterId,
                content,
                attachmentUrls
        );
    }
}
