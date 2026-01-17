package econovation.moongtaengi.study.domain.submission;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SubmissionAuthorityValidatorTest {

    private final SubmissionAuthorityValidator validator = new SubmissionAuthorityValidator();

    @Test
    @DisplayName("제출자와 과제 담당자가 일치하지 않으면 예외가 발생한다")
    void 권한_검증_실패() {
        //given
        Long submitterId = 1L;
        Long assigneeId = 2L;

        //when&then
        assertThatThrownBy(() -> validator.validate(submitterId, assigneeId))
                .isInstanceOf(SubmissionException.class)
                .extracting("errorCode")
                .isEqualTo(SubmissionErrorCode.NOT_ASSIGNEE);
    }

    @Test
    @DisplayName("제출자와 과제 담당자가 일치하면 검증을 통과한다")
    void 권한_검증_성공() {
        //given
        Long submitterId = 1L;
        Long assigneeId = 1L;

        //when&then
        assertDoesNotThrow(() -> validator.validate(submitterId, assigneeId));
    }

}
