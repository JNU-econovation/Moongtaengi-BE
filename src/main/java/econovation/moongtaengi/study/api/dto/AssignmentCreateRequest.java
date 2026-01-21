package econovation.moongtaengi.study.api.dto;

import econovation.moongtaengi.study.application.assignment.CreateAssignmentCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AssignmentCreateRequest(
        @NotNull(message = "프로세스 ID는 필수입니다.")
        Long processId,

        @NotNull(message = "담당자 ID는 필수입니다.")
        Long assigneeId,

        @NotBlank(message = "과제 내용은 필수입니다.")
        String content,

        LocalDateTime deadLine
) {
        public CreateAssignmentCommand toCommand(Long requesterId) {
                return CreateAssignmentCommand.builder()
                        .processId(this.processId)
                        .requesterId(requesterId)
                        .assigneeId(this.assigneeId)
                        .description(this.content)
                        .deadline(this.deadLine)
                        .build();
        }
}
