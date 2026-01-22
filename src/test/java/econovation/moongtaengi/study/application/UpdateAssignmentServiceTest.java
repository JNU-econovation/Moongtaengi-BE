package econovation.moongtaengi.study.application;

import static econovation.moongtaengi.study.domain.assignment.AssignmentFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import econovation.moongtaengi.study.application.assignment.UpdateAssignmentService;
import econovation.moongtaengi.study.application.assignment.UpdateDescriptionCommand;
import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentErrorCode;
import econovation.moongtaengi.study.domain.assignment.AssignmentException;
import econovation.moongtaengi.study.domain.assignment.AssignmentManagementPolicy;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider.ProcessInfo;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UpdateAssignmentServiceTest {

        @Mock
        private AssignmentRepository repository;
        @Mock
        private AssignmentManagementPolicy policy;
        @Mock
        private ProcessInfoProvider infoProvider;

        @InjectMocks
        private UpdateAssignmentService updateAssignmentService;

        @Test
        @DisplayName("방장은 과제 설명을 수정할 수 있다")
        void 과제_설명_수정_성공() {
            //given
            Long assignmentId = 1L;
            Long requesterId = 10L;
            Long processId = 50L;
            Long studyId = 99L;
            String newDescriptionValue = "수정된 과제 내용";

            UpdateDescriptionCommand command = UpdateDescriptionCommand.of(
                    assignmentId, requesterId, newDescriptionValue
            );

            Assignment assignment = anAssignment()
                    .processId(processId)
                    .build();

            ProcessInfo processInfo = new ProcessInfo(studyId,
                    LocalDate.now(), LocalDate.now().plusDays(7));

            given(repository.findById(assignmentId)).willReturn(Optional.of(assignment));
            given(infoProvider.getProcessInfo(processId)).willReturn(processInfo);

            //when
            updateAssignmentService.updateDescription(command);

            //then
            verify(infoProvider).getProcessInfo(processId);
            verify(policy).validateAuthority(studyId, requesterId);
            assertThat(assignment.getDescription().getValue()).isEqualTo(newDescriptionValue);
        }

        @Test
        @DisplayName("존재하지 않는 과제는 수정할 수 없다")
        void 과제_설명_수정_실패_과제_미존재() {
            //given
            Long nonExistentId = 999L;
            UpdateDescriptionCommand command = UpdateDescriptionCommand.of(
                    nonExistentId,
                    10L,
                    "수정하려는 내용"
            );

            given(repository.findById(nonExistentId)).willReturn(Optional.empty());

            //when&then
            assertThatThrownBy(() -> updateAssignmentService.updateDescription(command))
                    .isInstanceOf(AssignmentException.class)
                    .extracting("errorCode")
                    .isEqualTo(AssignmentErrorCode.ASSIGNMENT_NOT_FOUND);

            verify(infoProvider, never()).getProcessInfo(any());
            verify(policy, never()).validateAuthority(any(), any());
        }
}
