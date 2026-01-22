package econovation.moongtaengi.study.application.assignment;

import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentDescription;
import econovation.moongtaengi.study.domain.assignment.AssignmentDeadline;
import econovation.moongtaengi.study.domain.assignment.AssignmentManagementPolicy;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.assignment.AssignmentUniquenessValidator;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider.ProcessInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateAssignmentService {

    private final AssignmentManagementPolicy policy;
    private final AssignmentUniquenessValidator validator;
    private final AssignmentRepository repository;
    private final ProcessInfoProvider infoProvider;

    @Transactional
    public Long createAssignment(CreateAssignmentCommand command) {
        ProcessInfo processInfo = infoProvider.getProcessInfo(command.processId());

        policy.validateAllocation(processInfo.studyId(), command.requesterId(), command.assigneeId());

        validator.validate(command.processId(), command.assigneeId());



        AssignmentDeadline deadline = AssignmentDeadline.create(
                command.deadline(),
                processInfo.startDate(),
                processInfo.endDate()
        );

        AssignmentDescription description = new AssignmentDescription(command.description());

        Assignment assignment = Assignment.builder()
                .processId(command.processId())
                .assigneeId(command.assigneeId())
                .description(description)
                .deadline(deadline)
                .build();

        Assignment savedAssignment = repository.save(assignment);

        log.info("과제 생성 성공 - assignmentId: {}, processId: {}, requesterId: {}, assigneeId: {}",
                savedAssignment.getId(),
                command.processId(),
                command.requesterId(),
                command.assigneeId()
        );

        return savedAssignment.getId();
    }
}
