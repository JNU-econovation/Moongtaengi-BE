package econovation.moongtaengi.study.application.assignment;

import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentContent;
import econovation.moongtaengi.study.domain.assignment.AssignmentDeadline;
import econovation.moongtaengi.study.domain.assignment.AssignmentManagementPolicy;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.assignment.AssignmentUniquenessValidator;
import econovation.moongtaengi.study.domain.assignment.ProcessDateRangeProvider;
import econovation.moongtaengi.study.domain.assignment.ProcessDateRangeProvider.DateRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAssignmentService {

    private final AssignmentManagementPolicy policy;
    private final AssignmentUniquenessValidator validator;
    private final AssignmentRepository repository;
    private final ProcessDateRangeProvider dateRangeProvider;

    public Long createAssignment(CreateAssignmentCommand command) {

        policy.validate(command.studyId(), command.requesterId());

        validator.validate(command.processId(), command.assigneeId());

        DateRange dateRange = dateRangeProvider.getDateRange(command.processId());

        AssignmentDeadline deadline = AssignmentDeadline.create(
                command.deadline(),
                dateRange.startDate(),
                dateRange.endDate()
        );

        AssignmentContent content = new AssignmentContent(command.content());

        Assignment assignment = Assignment.builder()
                .processId(command.processId())
                .assigneeId(command.assigneeId())
                .content(content)
                .deadline(deadline)
                .build();

        return repository.save(assignment).getId();
    }
}
