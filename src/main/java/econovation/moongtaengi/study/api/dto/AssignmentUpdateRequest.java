package econovation.moongtaengi.study.api.dto;

import econovation.moongtaengi.study.application.assignment.UpdateDescriptionCommand;
import jakarta.validation.constraints.NotBlank;

public record AssignmentUpdateRequest(
        @NotBlank(message = "수정할 과제 내용은 필수입니다.")
        String description
) {
    public UpdateDescriptionCommand toCommand(Long assignmentId, Long requesterId) {
        return UpdateDescriptionCommand.of(assignmentId, requesterId, description);
    }
}
