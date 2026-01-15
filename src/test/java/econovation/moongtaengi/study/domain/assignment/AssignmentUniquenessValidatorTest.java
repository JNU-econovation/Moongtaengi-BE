package econovation.moongtaengi.study.domain.assignment;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AssignmentUniquenessValidatorTest {
    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private AssignmentUniquenessValidator validator;

    @Test
    @DisplayName("해당 프로세스에 멤버에게 이미 할당된 과제가 있으면 예외를 던진다")
    void 중복_할당_검증_실패() {
        //given
        Long processId = 100L;
        Long assigneeId = 10L;

        given(assignmentRepository.existsByProcessIdAndAssigneeId(processId, assigneeId))
                .willReturn(true);

        //when&then
        assertThatThrownBy(() -> validator.validate(processId, assigneeId))
                .isInstanceOf(AssignmentException.class)
                .extracting("errorCode")
                .isEqualTo(AssignmentErrorCode.ASSIGNMENT_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("중복된 과제가 없으면 검증을 통과한다")
    void 중복_할당_검증_성공() {
        //given
        Long processId = 100L;
        Long assigneeId = 10L;

        given(assignmentRepository.existsByProcessIdAndAssigneeId(processId, assigneeId))
                .willReturn(false);

        //when&then
        assertDoesNotThrow(() -> validator.validate(processId, assigneeId));
    }
}
