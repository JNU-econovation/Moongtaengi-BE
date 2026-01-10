package econovation.moongtaengi.study.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import econovation.moongtaengi.study.domain.InviteCode;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.StudyName;
import econovation.moongtaengi.study.domain.StudyPeriod;
import econovation.moongtaengi.study.domain.StudyPeriodValidator;
import econovation.moongtaengi.study.domain.StudyRepository;
import econovation.moongtaengi.study.domain.StudyTopic;
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
public class UpdateStudyServiceTest {
    @InjectMocks
    private UpdateStudyService updateStudyService;

    @Mock
    private StudyRepository studyRepository;

    @Mock
    private StudyPeriodValidator studyPeriodValidator;

    @Test
    @DisplayName("방장은 스터디 정보를 수정할 수 있다. (일부 필드만 수정 시 기존 값 유지)")
    void 스터디_수정_성공() {
        //given
        Long hostId = 1L;
        Long studyId = 100L;

        Study study = createStudy(hostId, studyId);

        given(studyRepository.findByIdWithMembers(studyId))
                .willReturn(Optional.of(study));

        UpdateStudyCommand command = new UpdateStudyCommand(
                hostId,
                studyId,
                "수정된 이름",
                null,
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 20));

        //when
        updateStudyService.updateStudy(command);

        //then
        verify(studyPeriodValidator).validate(eq(studyId), any(StudyPeriod.class));
        assertThat(study.getName().getValue()).isEqualTo("수정된 이름");
        assertThat(study.getTopic().getValue()).isEqualTo("테스트 주제");
        assertThat(study.getPeriod().getStartDate()).isEqualTo(LocalDate.of(2026, 1, 10));
        assertThat(study.getPeriod().getEndDate()).isEqualTo(LocalDate.of(2026, 1, 20));
    }

    @Test
    @DisplayName("존재하지 않는 스터디를 수정하려 하면 예외가 발생한다.")
    void 스터디_없음_예외() {
        //given
        Long hostId = 1L;
        Long studyId = 999L;
        given(studyRepository.findByIdWithMembers(studyId))
                .willReturn(Optional.empty());

        UpdateStudyCommand command = new UpdateStudyCommand(
                hostId,
                studyId,
                "수정된 이름",
                "수정된 주제",
                LocalDate.now(),
                LocalDate.now()
        );

        //when&then
        assertThatThrownBy(() -> updateStudyService.updateStudy(command))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.STUDY_NOT_FOUND);
    }

    @Test
    @DisplayName("방장이 아닌 멤버가 수정을 시도하면 예외가 발생한다.(도메인 예외 전파 테스트)")
    void 스터디_수정_실패_권한없음() {
        //given
        Long hostId = 1L;
        Long guestId = 50L;
        Long studyId = 100L;
        Study study = createStudy(hostId, studyId);
        study.addGuest(guestId);

        given(studyRepository.findByIdWithMembers(studyId))
                .willReturn(Optional.of(study));

        UpdateStudyCommand command = new UpdateStudyCommand(
                guestId,
                studyId,
                "수정된 이름",
                "수정된 주제",
                LocalDate.now(),
                LocalDate.now()
        );

        //when&then
        assertThatThrownBy(() -> updateStudyService.updateStudy(command))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.NOT_STUDY_HOST);
    }

    private Study createStudy(Long hostId, Long studyId) {
        Study study = new Study(
                new StudyName("테스트 이름"),
                new StudyPeriod(LocalDate.now(), LocalDate.now().plusDays(7)),
                new StudyTopic("테스트 주제"),
                hostId,
                new InviteCode("12345678")
        );
        ReflectionTestUtils.setField(study, "id", studyId);
        return study;
    }
}
