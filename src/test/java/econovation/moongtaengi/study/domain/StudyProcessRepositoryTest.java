package econovation.moongtaengi.study.domain;

import static org.assertj.core.api.Assertions.assertThat;

import econovation.moongtaengi.study.domain.process.StudyProcess;
import econovation.moongtaengi.study.domain.process.StudyProcessPeriodBound;
import econovation.moongtaengi.study.domain.process.StudyProcessRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class StudyProcessRepositoryTest {
    @Autowired
    private StudyProcessRepository studyProcessRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Test
    @DisplayName("스터디 ID로 조회 시 프로세스들의 전체 기간(시작~종료) 경계를 반환한다")
    void 프로세스_기간_경계_조회() {
        //given
        Study study = new Study(
                new StudyName("테스트 이름"),
                new StudyPeriod(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 30)),
                new StudyTopic("테스트 주제"),
                1L,
                new InviteCode("12345678")
        );
        studyRepository.save(study);
        Long studyId = study.getId();

        createAndSaveProcess(studyId, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10));
        createAndSaveProcess(studyId, LocalDate.of(2026, 1, 11), LocalDate.of(2026, 1, 20));
        createAndSaveProcess(studyId, LocalDate.of(2026, 1, 21), LocalDate.of(2026, 1, 30));

        //when
        StudyProcessPeriodBound bound = studyProcessRepository.findProcessPeriodBound(studyId);

        //then
        assertThat(bound).isNotNull();
        assertThat(bound.minStartDate()).isEqualTo(LocalDate.of(2026, 1, 1));
        assertThat(bound.maxEndDate()).isEqualTo(LocalDate.of(2026, 1, 30));
    }

    @Test
    @DisplayName("프로세스가 없으면 null을 반환한다 (min/max 결과가 null임)")
    void 프로세스_없음() {
        //given
        Long emptyStudyId = 99999L;

        //when
        StudyProcessPeriodBound bound = studyProcessRepository.findProcessPeriodBound(emptyStudyId);

        //then
        assertThat(bound.minStartDate()).isNull();
        assertThat(bound.maxEndDate()).isNull();
    }

    private void createAndSaveProcess(Long studyId, LocalDate startDate, LocalDate endDate) {
        StudyProcess studyProcess = StudyProcess.create(
                studyId,
                1,
                "테스트 프로세스",
                startDate,
                endDate,
                "테스트 과제 설명"
        );

        studyProcessRepository.save(studyProcess);
    }
}
