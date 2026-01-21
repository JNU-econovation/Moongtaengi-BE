package econovation.moongtaengi.study.application.assignment;

import econovation.moongtaengi.study.domain.assignment.*;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider.ProcessInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApproveAssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentManagementPolicy assignmentManagementPolicy;
    private final ProcessInfoProvider processInfoProvider;

    @Transactional
    public void approveAssignment(Long assignmentId, Long requesterId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentException(AssignmentErrorCode.ASSIGNMENT_NOT_FOUND));

        ProcessInfo processInfo = processInfoProvider.getProcessInfo(assignment.getProcessId());

        assignmentManagementPolicy.validate(processInfo.studyId(), requesterId);

        assignment.approve();
        assignmentRepository.save(assignment);

        log.info("과제 승인 성공 - assignmentId: {}, requesterId: {}, studyId: {}",
                assignmentId,
                requesterId,
                processInfo.studyId()
        );
    }
}
