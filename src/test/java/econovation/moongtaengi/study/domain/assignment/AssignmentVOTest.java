package econovation.moongtaengi.study.domain.assignment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class AssignmentVOTest {
    @Nested
    @DisplayName("과제 내용")
    class AssignmentTitleTest {
        @Test
        @DisplayName("과제 내용을 성공적으로 생성한다.")
        void 과제_내용_성공() {
            String value = "테스트 내용";
            AssignmentContent description = new AssignmentContent(value);

            assertThat(description.getValue()).isEqualTo(value);
        }

        @Test
        @DisplayName("앞뒤 공백은 자동으로 제거된다.")
        void 과제_내용_공백_제거() {
            String value = "  공백이 많은 내용  ";
            AssignmentContent title = new AssignmentContent(value);

            assertThat(title.getValue()).isEqualTo("공백이 많은 내용");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("과제 내용은 null이거나 비어있을 수 없다")
        void 과제_내용_비었음_실패(String invalidInput) {
            assertThatThrownBy(() -> new AssignmentContent(invalidInput))
                    .isInstanceOf(AssignmentException.class)
                    .extracting("errorCode")
                    .isEqualTo(AssignmentErrorCode.INVALID_ASSIGNMENT_INFO);
        }

        @Test
        @DisplayName("과제 내용은 300자를 초과할 수 없다")
        void 과제_내용_초과_실패() {
            String longContent = "뭉".repeat(400);

            assertThatThrownBy(() -> new AssignmentContent(longContent))
                    .isInstanceOf(AssignmentException.class)
                    .extracting("errorCode")
                    .isEqualTo(AssignmentErrorCode.INVALID_ASSIGNMENT_INFO);
        }
    }

    @Nested
    @DisplayName("과제 마감일")
    class AssignmentDeadlineTest {

        @Test
        @DisplayName("마감일 생성 성공 (기간 내 포함)")
        void 마감일_생성_성공() {
            //given
            LocalDate startDate = LocalDate.of(2026, 1, 1);
            LocalDate endDate = LocalDate.of(2026, 1, 31);
            LocalDateTime targetDate = LocalDateTime.of(2026, 1, 15, 12, 0);

            //when
            AssignmentDeadline deadline = AssignmentDeadline.create(targetDate, startDate, endDate);

            //then
            assertThat(deadline.getValue()).isEqualTo(targetDate);
        }

        @Test
        @DisplayName("마감일이 null이면 프로세스 종료일의 마지막 시간으로 자동 설정된다")
        void 마감일_자동_생성_성공() {
            //given
            LocalDate startDate = LocalDate.of(2026, 1, 1);
            LocalDate endDate = LocalDate.of(2026, 1, 31);
            LocalDateTime expectedDefault = LocalDateTime.of(2026, 1, 31, 23, 59, 59);

            //when
            AssignmentDeadline deadline = AssignmentDeadline.create(null, startDate, endDate);

            //then
            assertThat(deadline.getValue()).isEqualTo(expectedDefault);
        }

        @Test
        @DisplayName("마감일이 프로세스 기간을 벗어나면 예외가 발생한다")
        void 마감일_프로세스_기간_벗어남_실패() {
            //given
            LocalDate startDate = LocalDate.of(2026, 1, 1);
            LocalDate endDate = LocalDate.of(2026, 1, 31);
            LocalDateTime outOfRangeDate = LocalDateTime.of(2026, 2, 1, 0, 0);

            //when&then
            assertThatThrownBy(() -> AssignmentDeadline.create(outOfRangeDate, startDate, endDate))
                    .isInstanceOf(AssignmentException.class)
                    .extracting("errorCode")
                    .isEqualTo(AssignmentErrorCode.INVALID_ASSIGNMENT_INFO);
        }
    }
}
