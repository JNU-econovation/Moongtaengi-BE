package econovation.moongtaengi.study.application.assignment;

import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentErrorCode;
import econovation.moongtaengi.study.domain.assignment.AssignmentException;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.submission.SubmissionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubmissionCreatedEventHandler {

    private final AssignmentRepository assignmentRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(SubmissionCreatedEvent event) {
        log.info("과제 제출 이벤트 수신 - assignmentId: {}, isLate: {}", event.assignmentId(), event.isLate());

        Assignment assignment = assignmentRepository.findById(event.assignmentId())
                .orElseThrow(() -> new AssignmentException(AssignmentErrorCode.INVALID_ASSIGNMENT_INFO));

        assignment.markAsSubmitted(event.isLate());
    }
}
