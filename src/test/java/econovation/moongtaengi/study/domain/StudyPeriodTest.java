package econovation.moongtaengi.study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StudyPeriodTest {
    @Test
    @DisplayName("정상적인 기간(1 이상 730 이하)은 생성된다")
    void 기간_생성_성공() {
        // given
        LocalDate start = LocalDate.of(2025, 12, 1);
        LocalDate end = LocalDate.of(2025, 12, 31);

        // when
        StudyPeriod period = new StudyPeriod(start, end);

        // then
        assertThat(period.getTotalDays()).isEqualTo(31);
    }

    @Test
    @DisplayName("시작일과 종료일이 같아도(1일) 생성된다")
    void 당일치기_생성_성공() {
        // given
        LocalDate date = LocalDate.of(2025, 12, 1);

        // when
        StudyPeriod period = new StudyPeriod(date, date);

        // then
        assertThat(period.getTotalDays()).isEqualTo(1);
    }

    @Test
    @DisplayName("시작일이나 종료일이 null이면 예외가 발생한다")
    void 기간_Null_체크() {
        //given
        LocalDate now = LocalDate.now();
        LocalDate nullDate = null;

        //when&then
        assertThatThrownBy(() -> new StudyPeriod(nullDate, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작일과 종료일은 필수입니다.");

        assertThatThrownBy(() -> new StudyPeriod(now, nullDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작일과 종료일은 필수입니다.");
    }

    @Test
    @DisplayName("종료일이 시작일보다 빠르면 예외가 발생한다")
    void 날짜_순서_체크() {
        // given
        LocalDate start = LocalDate.of(2025, 12, 1);
        LocalDate end = LocalDate.of(2025, 11, 1);

        // when & then
        assertThatThrownBy(() -> new StudyPeriod(start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("종료일이 시작일보다 앞에 있을 수 없습니다.");
    }

    @Test
    @DisplayName("기간이 730일을 초과하면 예외가 발생한다")
    void 최대_기간_체크() {
        // given
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = start.plusDays(730); // 731일째 (1일 초과)

        // when & then
        assertThatThrownBy(() -> new StudyPeriod(start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("730 이하");
    }

}
