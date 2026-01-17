package econovation.moongtaengi.study.infra;

import static econovation.moongtaengi.study.domain.assignment.AssignmentFixture.anAssignment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import econovation.moongtaengi.study.domain.assignment.Assignment;
import econovation.moongtaengi.study.domain.assignment.AssignmentRepository;
import econovation.moongtaengi.study.domain.submission.AssignmentInfoProvider.AssignmentInfo;
import econovation.moongtaengi.study.domain.submission.SubmissionErrorCode;
import econovation.moongtaengi.study.domain.submission.SubmissionException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AssignmentInfoProviderImplTest {
    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private AssignmentInfoProviderImpl assignmentInfoProvider;

    @Test
    @DisplayName("과제 ID로 과제 정보를 조회하여 DTO로 반환한다")
    void 과제_정보_조회_성공() {
        //given
        Long assignmentId = 1L;

        Assignment assignment = anAssignment()
                .processId(10L)
                .assigneeId(20L)
                .build();

        given(assignmentRepository.findById(assignmentId)).willReturn(Optional.of(assignment));

        //when
        AssignmentInfo result = assignmentInfoProvider.getAssignmentInfo(assignmentId);

        //then
        assertThat(result.assigneeId()).isEqualTo(20L);
        assertThat(result.deadline()).isEqualTo(assignment.getDeadline().getValue());
    }

    @Test
    @DisplayName("존재하지 않는 과제 조회 시 예외가 발생한다")
    void 존재하지_않는_과제_예외() {
        // given
        Long invalidId = 999L;

        given(assignmentRepository.findById(invalidId)).willReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> assignmentInfoProvider.getAssignmentInfo(invalidId))
                .isInstanceOf(SubmissionException.class)
                .extracting("errorCode")
                .isEqualTo(SubmissionErrorCode.INVALID_ASSIGNMENT_ID);
    }
}
