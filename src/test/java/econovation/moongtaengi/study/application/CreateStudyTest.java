package econovation.moongtaengi.study.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import econovation.moongtaengi.member.application.ExperienceService;
import econovation.moongtaengi.study.api.dto.StudyCreateRequest;
import econovation.moongtaengi.study.domain.InviteCode;
import econovation.moongtaengi.study.domain.Study;
import econovation.moongtaengi.study.domain.StudyCreationValidator;
import econovation.moongtaengi.study.domain.StudyErrorCode;
import econovation.moongtaengi.study.domain.StudyException;
import econovation.moongtaengi.study.domain.StudyFactory;
import econovation.moongtaengi.study.domain.StudyName;
import econovation.moongtaengi.study.domain.StudyPeriod;
import econovation.moongtaengi.study.domain.StudyRepository;
import econovation.moongtaengi.study.domain.StudyTopic;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class CreateStudyTest {
    @Mock
    private StudyCreationValidator studyCreationValidator;

    @Mock
    private StudyFactory studyFactory;

    @Mock
    private StudyRepository studyRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private CreateStudyService createStudyService;

    private StudyCreateRequest commonCreateRequest;
    private Long commonMemberId;

    @BeforeEach
    void setUp() {
        commonMemberId = 1L;
        commonCreateRequest = new StudyCreateRequest(
                "스프링부트 스터디",
                "JPA",
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );
    }

    @Test
    @DisplayName("스터디 생성 요청이 오면 Factory를 통해 객체를 생성하고 Repository에 저장한다.")
    void 스터디_생성_성공() {
        // given
        Study expectedStudy = new Study(
                new StudyName(commonCreateRequest.name()),
                new StudyPeriod(commonCreateRequest.startDate(), commonCreateRequest.endDate()),
                new StudyTopic(commonCreateRequest.topic()),
                commonMemberId,
                InviteCode.generate()
        );
        ReflectionTestUtils.setField(expectedStudy, "id", 1L);
        given(studyFactory.createStudy(any(), any(), any(), any(), any()))
                .willReturn(expectedStudy);

        //when
        Long studyId = createStudyService.createStudy(commonMemberId,
                commonCreateRequest.name(),
                commonCreateRequest.topic(),
                commonCreateRequest.startDate(),
                commonCreateRequest.endDate());

        //then
        verify(studyFactory).createStudy(
                commonMemberId,
                commonCreateRequest.name(),
                commonCreateRequest.startDate(),
                commonCreateRequest.endDate(),
                commonCreateRequest.topic()
        );
        verify(studyRepository).save(expectedStudy);
        assertThat(studyId).isEqualTo(expectedStudy.getId());
    }

    @Test
    @DisplayName("validator가 임시회원이라는 이유로 예외를 던지면 스터디 생성 로직이 중단된다.")
    void 스터디_임시회원_실패_예외발생() {
        //given
        willThrow(new StudyException(StudyErrorCode.STUDY_CREATION_DENIED_TEMP_MEMBER))
                .given(studyCreationValidator).validate(commonMemberId);


        //when&then
        assertThatThrownBy(() -> createStudyService.createStudy(
                commonMemberId,
                commonCreateRequest.name(),
                commonCreateRequest.topic(),
                commonCreateRequest.startDate(),
                commonCreateRequest.endDate()
        ))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.STUDY_CREATION_DENIED_TEMP_MEMBER);

        verify(studyRepository, never()).save(any());
    }
}
