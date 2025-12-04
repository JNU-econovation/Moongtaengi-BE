package econovation.moongtaengi.study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import econovation.moongtaengi.member.domain.NicknameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class StudyNameTest {
    @Test
    @DisplayName("정상적인 스터디 이름은 생성된다 (한글, 영어, 숫자, 공백 허용)")
    void 스터디이름_생성_성공() {
        // given
        String validName = "Sp링 스터디 1기";

        // when
        StudyName studyName = new StudyName(validName);

        // then
        assertThat(studyName.getValue()).isEqualTo(validName);
    }

    @Test
    @DisplayName("앞뒤 공백은 자동으로 제거되어 저장된다")
    void 스터디이름_공백제거_테스트() {
        // given
        String untrimmedName = "  뭉탱이  ";

        // when
        StudyName studyName = new StudyName(untrimmedName);

        // then
        assertThat(studyName.getValue()).isEqualTo("뭉탱이");
    }

    @Test
    @DisplayName("스터디 이름은 null이거나 비어있을 수 없다")
    void 스터디이름_null_빈값_체크() {
        //given
        String nullValue = null;
        String blankValue = "   ";

        //when&then
        assertThatThrownBy(() -> new StudyName(nullValue))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.NAME_NOT_BLANK);

        assertThatThrownBy(() -> new StudyName(blankValue))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.NAME_NOT_BLANK);
    }

    @Test
    @DisplayName("스터디 이름 길이는 2자 이상 10자 이하여야 한다")
    void 스터디이름_길이_체크() {
        //given
        String shortName = "A";
        String longName = "뭉탱이탱이탱이탱이탱이탱이";

        //when&then
        assertThatThrownBy(() -> new StudyName(shortName))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.NAME_LENGTH_INVALID);

        assertThatThrownBy(() -> new StudyName(longName))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.NAME_LENGTH_INVALID);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Study!", "뭉탱이@", "#코딩"}) //given
    @DisplayName("특수문자가 포함되면 예외가 발생한다")
    void 스터디이름_형식_체크(String invalidValue) {
        //when&then
        assertThatThrownBy(() -> new StudyName(invalidValue))
                .isInstanceOf(StudyException.class)
                .extracting("errorCode")
                .isEqualTo(StudyErrorCode.NAME_PATTERN_INVALID);
    }
}
