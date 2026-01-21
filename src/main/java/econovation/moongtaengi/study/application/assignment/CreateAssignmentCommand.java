package econovation.moongtaengi.study.application.assignment;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CreateAssignmentCommand(
        Long processId,
        Long requesterId,
        Long assigneeId,
        String description,
        LocalDateTime deadline
) {
}
