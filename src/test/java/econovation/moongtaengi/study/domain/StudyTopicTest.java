package econovation.moongtaengi.study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StudyTopicTest {
    @Test
    @DisplayName("정상적인 주제는 생성된다")
    void 주제_생성_성공() {
        StudyTopic topic = new StudyTopic("스프링 부트");
        assertThat(topic.getValue()).isEqualTo("스프링 부트");
    }

    @Test
    @DisplayName("주제의 앞뒤 공백은 제거된다")
    void 주제_공백_제거() {
        //given
        String untrimmed = "   JPA    ";

        //when
        StudyTopic topic = new StudyTopic(untrimmed);

        //then
        assertThat(topic.getValue()).isEqualTo("JPA");
    }

    @Test
    @DisplayName("주제가 비어있으면 예외가 발생한다")
    void 주제_빈값_체크() {
        //given
        String nullValue = null;
        String blankValue = "   ";

        //when&then
        assertThatThrownBy(() -> new StudyTopic(nullValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("필수");

        assertThatThrownBy(() -> new StudyTopic(blankValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("필수");
    }

    @Test
    @DisplayName("주제가 50자를 넘으면 예외가 발생한다")
    void 주제_길이_체크() {
        String longTopic = "1234567890123456789011238746178236478126348716238471623874612387461412341234231123412341234123412341234321";
        assertThatThrownBy(() -> new StudyTopic(longTopic))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("50자");
    }
}
