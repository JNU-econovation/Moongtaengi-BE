package econovation.moongtaengi.study.domain.assignment;

import static econovation.moongtaengi.study.domain.assignment.AssignmentFixture.anAssignment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AssignmentTest {
    @Test
    @DisplayName("기본 픽스처로 생성 성공")
    void create_success_fixture() {
        //when
        Assignment assignment = anAssignment().build();

        //then
        assertThat(assignment.getContent())
                .isEqualTo(AssignmentFixture.DEFAULT_CONTENT);
    }

    @Test
    @DisplayName("특정 담당자에게 개별 과제를 성공적으로 생성한다")
    void 과제_생성_성공() {
        //given
        Long newAssigneeId = 10L;

        //when
        Assignment assignment = anAssignment()
                .assigneeId(newAssigneeId)
                .build();

        //then
        assertThat(assignment.getAssigneeId()).isEqualTo(newAssigneeId);
        assertThat(assignment.getContent())
                .isEqualTo(AssignmentFixture.DEFAULT_CONTENT);
    }

    @Test
    @DisplayName("과제 제출 시 상태가 SUBMITTED로 변경되고, 지각 여부가 기록된다.")
    void 과제_제출_상태_변경_지각_여부_기록_성공() {
        //given
        Assignment assignment = anAssignment()
                .build();

        //when
        boolean isLate = true;
        assignment.markAsSubmitted(isLate);

        //then
        assertThat(assignment.getStatus()).isEqualTo(AssignmentStatus.SUBMITTED);
        assertThat(assignment.isLate()).isTrue();
    }

    @Test
    @DisplayName("제출된 과제는 승인 상태로 변경할 수 있다.")
    void 과제_승인_성공() {
        //given
        Assignment assignment = anAssignment().build();
        assignment.markAsSubmitted(false);

        //when
        assignment.approve();

        //then
        assertThat(assignment.getStatus()).isEqualTo(AssignmentStatus.APPROVED);
    }

    @Test
    @DisplayName("제출되지 않은 과제는 승인할 수 없다.")
    void 과제_승인_미제출_상태_실패() {
        //given
        Assignment assignment = anAssignment().build();

        //when&then
        assertThatThrownBy(() -> assignment.approve())
                .isInstanceOf(AssignmentException.class)
                .extracting("errorCode")
                .isEqualTo(AssignmentErrorCode.CANNOT_APPROVE_NOT_SUBMITTED);
    }
}
