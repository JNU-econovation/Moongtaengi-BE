package econovation.moongtaengi.study.application;

import static econovation.moongtaengi.study.domain.assignment.AssignmentFixture.anAssignment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import econovation.moongtaengi.study.application.assignment.SubmissionCreatedEventHandler;
import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.assignment.AssignmentStatus;
import econovation.moongtaengi.study.domain.submission.SubmissionCreatedEvent;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class SubmissionCreatedEventHandlerTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private SubmissionCreatedEventHandler eventHandler;

    @Test
    @DisplayName("과제 제출 이벤트를 수신하면 과제 상태를 제출됨으로 변경한다")
    void 과제_상태_변경_성공() {
        //given
        Long assignmentId = 1L;
        Long submitterId = 2L;
        boolean isLate = true;

        SubmissionCreatedEvent event = new SubmissionCreatedEvent(assignmentId, submitterId, isLate);

        Assignment assignment = anAssignment().build();
        ReflectionTestUtils.setField(assignment, "id", assignmentId);

        given(assignmentRepository.findById(assignmentId))
                .willReturn(Optional.of(assignment));

        //when
        eventHandler.handle(event);

        //then
        assertThat(assignment.getStatus()).isEqualTo(AssignmentStatus.SUBMITTED);
        assertThat(assignment.isLate()).isTrue();

        verify(assignmentRepository).findById(assignmentId);
    }
}
