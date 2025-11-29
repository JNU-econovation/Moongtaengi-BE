package econovation.moongtaengi.study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

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
        given(studyRepository.countByMemberIdAndRole(eq(hostId), eq(StudyRole.HOST)))
                .willReturn(4);

        // when
        Study study = studyFactory.createStudy(hostId, studyName);

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
        given(studyRepository.countByMemberIdAndRole(eq(hostId), eq(StudyRole.HOST)))
                .willReturn(5);

        // when & then
        assertThatThrownBy(() -> studyFactory.createStudy(hostId, "새 스터디"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("5개까지만");
    }
}
