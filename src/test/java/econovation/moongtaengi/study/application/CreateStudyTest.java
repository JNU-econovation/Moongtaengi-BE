package econovation.moongtaengi.study.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import econovation.moongtaengi.study.api.dto.StudyCreateRequest;
import econovation.moongtaengi.study.domain.InviteCode;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyFactory;
import econovation.moongtaengi.study.domain.StudyName;
import econovation.moongtaengi.study.domain.StudyPeriod;
import econovation.moongtaengi.study.domain.StudyRepository;
import econovation.moongtaengi.study.domain.StudyTopic;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class CreateStudyTest {
    @Mock
    private StudyFactory studyFactory;

    @Mock
    private StudyRepository studyRepository;

    @InjectMocks
    private CreateStudyService createStudyService;

    @Test
    @DisplayName("스터디 생성 요청이 오면 Factory를 통해 객체를 생성하고 Repository에 저장한다.")
    void 스터디_생성_성공() {
        // given
        Long memberId = 1L;
        StudyCreateRequest request = new StudyCreateRequest(
                "스프링부트 스터디",
                "JPA",
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );
        Study expectedStudy = new Study(
                new StudyName(request.name()),
                new StudyPeriod(request.startDate(), request.endDate()),
                new StudyTopic(request.topic()),
                memberId,
                InviteCode.generate()
        );
        ReflectionTestUtils.setField(expectedStudy, "id", 1L);
        given(studyFactory.createStudy(any(), any(), any(), any(), any()))
                .willReturn(expectedStudy);

        //when
        Long studyId = createStudyService.createStudy(memberId,
                request.name(),
                request.topic(),
                request.startDate(),
                request.endDate());

        //then
        verify(studyFactory).createStudy(
                memberId,
                request.name(),
                request.startDate(),
                request.endDate(),
                request.topic()
        );
        verify(studyRepository).save(expectedStudy);
        assertThat(studyId).isEqualTo(expectedStudy.getId());
    }
}
