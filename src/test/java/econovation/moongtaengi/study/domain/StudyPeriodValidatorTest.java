package econovation.moongtaengi.study.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

import econovation.moongtaengi.study.domain.process.StudyProcessPeriodBound;
import econovation.moongtaengi.study.domain.process.StudyProcessRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StudyPeriodValidatorTest {
    @InjectMocks
    private StudyPeriodValidator studyPeriodValidator;

    @Mock
    private StudyProcessRepository studyProcessRepository;

    @Test
    @DisplayName("스터디 기간이 프로세스 기간을 모두 포함할 때 예외를 던지지 않는다.")
    void 검증_성공() {
        //given
        Long studyId = 1L;

        StudyProcessPeriodBound bound = new StudyProcessPeriodBound(
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 20)
        );
        given(studyProcessRepository.findProcessPeriodBound(studyId)).willReturn(bound);


        StudyPeriod newPeriod = new StudyPeriod(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 30)
        );

        //when&then
        assertDoesNotThrow(() -> studyPeriodValidator.validate(studyId, newPeriod));
    }

    @Test
    @DisplayName("스터디 종료일이 프로세스 종료일보다 빠를 때 예외를 던진다.")
    void 종료일_짧음_검증_실패() {
        //given
        Long studyId = 1L;

        StudyProcessPeriodBound bound = new StudyProcessPeriodBound(
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 20)
        );
        given(studyProcessRepository.findProcessPeriodBound(studyId)).willReturn(bound);

        StudyPeriod newPeriod = new StudyPeriod(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 19)
        );

        //when&then
        assertThatThrownBy(() -> studyPeriodValidator.validate(studyId, newPeriod))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.STUDY_END_DATE_TOO_EARLY);
    }

    @Test
    @DisplayName("스터디 시작일이 프로세스 시작일보다 늦을 때 예외를 던진다.")
    void 시작일_늦음_검증_실패() {
        //given
        Long studyId = 1L;

        StudyProcessPeriodBound bound = new StudyProcessPeriodBound(
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 20)
        );
        given(studyProcessRepository.findProcessPeriodBound(studyId)).willReturn(bound);

        StudyPeriod newPeriod = new StudyPeriod(
                LocalDate.of(2026, 1, 11),
                LocalDate.of(2026, 1, 30)
        );

        //when&then
        assertThatThrownBy(() -> studyPeriodValidator.validate(studyId, newPeriod))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.STUDY_START_DATE_TOO_LATE);
    }
}
