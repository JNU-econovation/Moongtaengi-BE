package econovation.moongtaengi.study.domain.assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignmentUniquenessValidator {

    private final AssignmentRepository assignmentRepository;

    public void validate(Long processId, Long assigneeId) {
        boolean exists = assignmentRepository.existsByProcessIdAndAssigneeId(processId, assigneeId);

        if (exists) {
            throw new AssignmentException(AssignmentErrorCode.ASSIGNMENT_ALREADY_EXISTS);
        }

    }
}
