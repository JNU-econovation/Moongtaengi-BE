package econovation.moongtaengi.study.application.assignment;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CreateAssignmentCommand(
        Long studyId,
        Long processId,
        Long requesterId,
        Long assigneeId,
        String content,
        LocalDateTime deadline
) {
}
