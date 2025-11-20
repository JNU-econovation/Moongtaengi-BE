package econovation.moongtaengi.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class NicknameTest {
    private static final String ERR_MSG_NULL = "닉네임은 비어있을 수 없습니다.";
    private static final String ERR_MSG_LENGTH = "자 이상";
    private static final String ERR_MSG_PATTERN = "닉네임은 한글과 숫자 조합으로만 가능합니다.";

    @Test
    @DisplayName("정상적인 닉네임은 생성되어야 한다.")
    void 닉네임_생성_성공() {
        Nickname nickname = new Nickname("뭉탱이1");
        assertThat(nickname.getValue()).isEqualTo("뭉탱이1");
    }

    @Test
    @DisplayName("닉네임은 null이거나 비어있을 수 없다")
    void 닉네임_null_빈값_체크() {
        assertThatThrownBy(() -> new Nickname(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERR_MSG_NULL);

        assertThatThrownBy(() -> new Nickname(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERR_MSG_NULL);
    }

    @Test
    @DisplayName("닉네임 길이는 2자 이상 7자 이하여야 한다.")
    void 닉네임_길이_체크() {
        assertThatThrownBy(() -> new Nickname("뭉"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ERR_MSG_LENGTH);

        assertThatThrownBy(() -> new Nickname("뭉탱이뭉탱이뭉탱이"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ERR_MSG_LENGTH);
    }

    @ParameterizedTest
    @ValueSource(strings = {"moong", "뭉탱이!", "abc23"})
    @DisplayName("닉네임이 한글 숫자 조합이 아닐 경우 예외가 발생한다.")
    void 닉네임_형식_체크(String invalidNickname) {
        assertThatThrownBy(() -> new Nickname(invalidNickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERR_MSG_PATTERN);
    }


}
