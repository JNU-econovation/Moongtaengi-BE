package econovation.moongtaengi.study.application.assignment;

import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentDescription;
import econovation.moongtaengi.study.domain.assignment.AssignmentErrorCode;
import econovation.moongtaengi.study.domain.assignment.AssignmentException;
import econovation.moongtaengi.study.domain.assignment.AssignmentManagementPolicy;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider.ProcessInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateAssignmentService {

    private final AssignmentRepository repository;
    private final AssignmentManagementPolicy policy;
    private final ProcessInfoProvider infoProvider;

    @Transactional
    public void updateDescription(UpdateDescriptionCommand command) {
        Assignment assignment = repository.findById(command.assignmentId())
                .orElseThrow(() -> new AssignmentException(AssignmentErrorCode.ASSIGNMENT_NOT_FOUND));

        ProcessInfo processInfo = infoProvider.getProcessInfo(assignment.getProcessId());

        policy.validateAuthority(processInfo.studyId(), command.requesterId());

        assignment.updateDescription(new AssignmentDescription(command.description()));

        log.info("과제 설명 수정 완료 - assignmentId: {}, requesterId: {}",
                assignment.getId(), command.requesterId());
    }
}
