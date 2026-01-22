package econovation.moongtaengi.study.application;

import static econovation.moongtaengi.study.domain.assignment.AssignmentFixture.anAssignment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import econovation.moongtaengi.study.application.assignment.ApproveAssignmentService;
import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentErrorCode;
import econovation.moongtaengi.study.domain.assignment.AssignmentException;
import econovation.moongtaengi.study.domain.assignment.AssignmentManagementPolicy;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.assignment.AssignmentStatus;
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
public class ApproveAssignmentServiceTest {

    @InjectMocks
    private ApproveAssignmentService approveAssignmentService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private AssignmentManagementPolicy assignmentManagementPolicy;

    @Mock
    private ProcessInfoProvider processInfoProvider;

    @Test
    @DisplayName("과제 승인할 때 관리자 권한이 확인되면 과제 상태가 APPROVED로 변경된다.")
    void 과제_승인_성공() {
        // given
        Long requesterId = 1L;
        Long studyId = 99L;
        Long processId = 10L;
        Long assignmentId = 100L;

        Assignment assignment = anAssignment()
                .processId(processId)
                .build();
        assignment.markAsSubmitted(false);

        given(assignmentRepository.findById(assignmentId))
                .willReturn(Optional.of(assignment));

        given(processInfoProvider.getProcessInfo(processId))
                .willReturn(new ProcessInfo(studyId, LocalDate.now(), LocalDate.now()));

        //when
        approveAssignmentService.approveAssignment(assignmentId, requesterId);

        //then
        assertThat(assignment.getStatus()).isEqualTo(AssignmentStatus.APPROVED);

        verify(assignmentManagementPolicy).validateAuthority(studyId, requesterId);
    }

    @Test
    @DisplayName("존재하지 않는 과제 ID로 승인 요청 시 예외가 발생한다.")
    void 과제_승인_실패_과제없음() {
        //given
        Long invalidAssignmentId = 999L;
        Long requesterId = 1L;

        given(assignmentRepository.findById(invalidAssignmentId))
                .willReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> approveAssignmentService.approveAssignment(invalidAssignmentId, requesterId))
                .isInstanceOf(AssignmentException.class)
                .extracting("errorCode")
                .isEqualTo(AssignmentErrorCode.ASSIGNMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("과제 관리자 권한이 없는 경우 검증기에서 예외를 던진다.")
    void 과제_승인_실패_권한없음() {
        //given
        Long assignmentId = 100L;
        Long requesterId = 2L;
        Long processId = 10L;
        Long studyId = 99L;

        Assignment assignment = anAssignment()
                .processId(processId)
                .build();

        given(assignmentRepository.findById(assignmentId))
                .willReturn(Optional.of(assignment));

        given(processInfoProvider.getProcessInfo(processId))
                .willReturn(new ProcessInfo(studyId, LocalDate.now(), LocalDate.now()));

        willThrow(new AssignmentException(AssignmentErrorCode.NO_MANAGEMENT_PERMISSION))
                .given(assignmentManagementPolicy).validateAuthority(studyId, requesterId);

        //when&then
        assertThatThrownBy(() -> approveAssignmentService.approveAssignment(assignmentId, requesterId))
                .isInstanceOf(AssignmentException.class)
                .extracting("errorCode")
                .isEqualTo(AssignmentErrorCode.NO_MANAGEMENT_PERMISSION);
    }
}
