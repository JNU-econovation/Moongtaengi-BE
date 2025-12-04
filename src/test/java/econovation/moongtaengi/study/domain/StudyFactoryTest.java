package econovation.moongtaengi.study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StudyFactoryTest {
    @Mock
    private StudyRepository studyRepository;

    @InjectMocks
    private StudyFactory studyFactory;

    @Test
    @DisplayName("내가 방장인 스터디가 5개 미만이면, 새 스터디 생성에 성공한다")
    void 스터디_생성_성공() {
        // given
        Long hostId = 1L;
        String studyName = "스프링 스터디";
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(7);
        String topic = "스프링부트 JPA";
        given(studyRepository.countByMemberIdAndRole(eq(hostId), eq(StudyRole.HOST)))
                .willReturn(4);
        given(studyRepository.existsByInviteCodeValue(anyString()))
                .willReturn(false);

        // when
        Study study = studyFactory.createStudy(hostId, studyName, start, end, topic);

        // then
        assertThat(study).isNotNull();
        assertThat(study.getName().getValue()).isEqualTo(studyName);
        assertThat(study.getMembers()).hasSize(1);
        assertThat(study.getMembers().get(0).getMemberId()).isEqualTo(hostId);
    }

    @Test
    @DisplayName("내가 방장인 스터디가 이미 5개라면, 예외가 발생한다")
    void 스터디_생성_제한_실패() {
        // given
        Long hostId = 1L;
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(7);
        String topic = "스프링부트 JPA";
        given(studyRepository.countByMemberIdAndRole(eq(hostId), eq(StudyRole.HOST)))
                .willReturn(5);

        // when & then
        assertThatThrownBy(() -> studyFactory.createStudy(hostId, "새 스터디", start, end, topic))
                .isInstanceOf(StudyCreateLimitException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.STUDY_CREATION_LIMIT_EXCEEDED);
    }

    @Test
    @DisplayName("초대 코드 생성 시 중복이 5번 연속 발생하면 예외가 발생한다")
    void 초대코드_재시도_초과_실패() {
        // given
        Long hostId = 1L;
        String studyName = "스프링 스터디";
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(7);
        String topic = "스프링부트 JPA";
        given(studyRepository.countByMemberIdAndRole(eq(hostId), eq(StudyRole.HOST)))
                .willReturn(4);
        given(studyRepository.existsByInviteCodeValue(anyString()))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> studyFactory.createStudy(hostId, studyName, start, end, topic))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("실패했습니다");
    }
}
