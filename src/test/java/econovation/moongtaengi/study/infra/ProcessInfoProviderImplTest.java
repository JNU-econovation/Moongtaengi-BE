package econovation.moongtaengi.study.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import econovation.moongtaengi.study.domain.assignment.AssignmentErrorCode;
import econovation.moongtaengi.study.domain.assignment.AssignmentException;
import econovation.moongtaengi.study.domain.assignment.ProcessInfoProvider.ProcessInfo;
import econovation.moongtaengi.study.domain.process.StudyProcess;
import econovation.moongtaengi.study.domain.process.StudyProcessRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ProcessInfoProviderImplTest {

    @Mock
    private StudyProcessRepository studyProcessRepository;

    @InjectMocks
    private ProcessInfoProviderImpl provider;


    @Test
    @DisplayName("프로세스 ID로 날짜 범위를 조회하면 DTO로 변환되어 반환된다")
    void 날짜_범위_조회_성공() {
        //given
        Long processId = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);

        StudyProcess process = StudyProcess.create(
                10L,
                1,
                "테스트 프로세스",
                startDate,
                endDate,
                "과제 설명"
        );
        ReflectionTestUtils.setField(process, "id", processId);

        given(studyProcessRepository.findById(processId))
                .willReturn(Optional.of(process));

        //when
        ProcessInfo result = provider.getProcessInfo(processId);

        //then
        assertThat(result.startDate()).isEqualTo(startDate);
        assertThat(result.endDate()).isEqualTo(endDate);
    }

    @Test
    @DisplayName("존재하지 않는 프로세스 ID 조회 시 예외가 발생한다")
    void 존재하지_않는_프로세스_예외() {
        //given
        Long invalidId = 999L;
        given(studyProcessRepository.findById(invalidId)).willReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> provider.getProcessInfo(invalidId))
                .isInstanceOf(AssignmentException.class)
                .extracting("errorCode")
                .isEqualTo(AssignmentErrorCode.INVALID_ASSIGNMENT_INFO);
    }
}
