package econovation.moongtaengi.study.domain.submission;

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
class SubmissionUniquenessValidatorTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @InjectMocks
    private SubmissionUniquenessValidator validator;

    @Test
    @DisplayName("해당 과제에 이미 제출 내역이 존재하면 예외가 발생한다")
    void 중복_제출_검증_실패() {
        //given
        Long assignmentId = 1L;
        given(submissionRepository.existsByAssignmentId(assignmentId))
                .willReturn(true);

        //when&then
        assertThatThrownBy(() -> validator.validate(assignmentId))
                .isInstanceOf(SubmissionException.class)
                .extracting("errorCode")
                .isEqualTo(SubmissionErrorCode.ALREADY_SUBMITTED);
    }

    @Test
    @DisplayName("제출 내역이 없으면 검증을 통과한다")
    void 중복_제출_검증_성공() {
        //given
        Long assignmentId = 1L;
        given(submissionRepository.existsByAssignmentId(assignmentId))
                .willReturn(false);

        //when&then
        assertDoesNotThrow(() -> validator.validate(assignmentId));
    }
}
