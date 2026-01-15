package econovation.moongtaengi.study.application;

import static econovation.moongtaengi.study.domain.assignment.AssignmentFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import econovation.moongtaengi.study.application.assignment.CreateAssignmentCommand;
import econovation.moongtaengi.study.application.assignment.CreateAssignmentService;
import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentDeadline;
import econovation.moongtaengi.study.domain.assignment.AssignmentManagementPolicy;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.assignment.AssignmentUniquenessValidator;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider.ProcessInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


@ExtendWith(MockitoExtension.class)
public class CreateAssignmentServiceTest {

    @Mock
    private AssignmentManagementPolicy policy;

    @Mock
    private AssignmentUniquenessValidator validator;

    @Mock
    private AssignmentRepository repository;

    @Mock
    private ProcessInfoProvider infoProvider;

    @InjectMocks
    private CreateAssignmentService createAssignmentService;

    @Test
    @DisplayName("권한과 중복 검사를 모두 통과하면 과제를 생성하고 저장한다")
    void 과제_생성_성공() {
        //given
        Long studyId = 1L;
        LocalDate processStartDate = LocalDate.now();
        LocalDate processEndDate = processStartDate.plusDays(14);
        LocalDateTime requestDeadline = processStartDate.plusDays(7).atStartOfDay();

        CreateAssignmentCommand command = CreateAssignmentCommand.builder()
                .processId(DEFAULT_PROCESS_ID)
                .requesterId(10L)
                .assigneeId(DEFAULT_ASSIGNEE_ID)
                .content(DEFAULT_CONTENT_VALUE)
                .deadline(requestDeadline)
                .build();

        Assignment savedAssignment = anAssignment()
                .deadline(
                        AssignmentDeadline.create(requestDeadline, processStartDate, processEndDate))
                .build();

        ReflectionTestUtils.setField(savedAssignment, "id", 1L);

        given(infoProvider.getProcessInfo(command.processId()))
                .willReturn(new ProcessInfo(studyId, processStartDate, processEndDate));

        given(repository.save(any(Assignment.class))).willReturn(savedAssignment);

        //when
        Long resultId = createAssignmentService.createAssignment(command);

        //then
        assertThat(resultId).isEqualTo(1L);

        verify(policy).validate(studyId, command.requesterId());
        verify(validator).validate(command.processId(), command.assigneeId());
        verify(infoProvider).getProcessInfo(command.processId());

        ArgumentCaptor<Assignment> captor = ArgumentCaptor.forClass(Assignment.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getDeadline().getValue()).isEqualTo(requestDeadline);
    }
}
