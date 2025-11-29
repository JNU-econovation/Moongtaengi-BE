package econovation.moongtaengi.study.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StudyTest {
    @Test
    @DisplayName("스터디 생성 시 이름이 올바르게 설정된다")
    void 스터디_생성_성공() {
        // given
        StudyName name = new StudyName("스프링 스터디");

        // when
        Study study = new Study(name);

        // then
        assertThat(study).isNotNull();
        assertThat(study.getName()).isEqualTo(name);
    }
}

