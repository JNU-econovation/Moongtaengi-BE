package econovation.moongtaengi.study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InviteCodeTest {
    private static final int LENGTH = 8;

    @Test
    @DisplayName("정상적인 초대코드가 발급된다.")
    void 초대코드_생성_발급_성공() {
        //when
        InviteCode inviteCode = InviteCode.generate();

        //then
        assertThat(inviteCode).isNotNull();
        assertThat(inviteCode.getValue()).hasSize(LENGTH);
    }

    @Test
    @DisplayName("초대 코드는 null일 수 없다")
    void 초대코드_생성_null_실패() {
        //given
        String code = null;

        //when&then
        assertThatThrownBy(() -> new InviteCode(code))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("초대코드가 길면")
    void 초대코드_생성_길이_실패() {
        //given
        String longCode = "1234567890";
        String shortCode = "1234";

        //when&then
        assertThatThrownBy(() -> new InviteCode(longCode))
                .isInstanceOf(IllegalStateException.class) // 아까 서버 에러로 처리하기로 했죠?
                .hasMessageContaining(LENGTH + "글자");


        assertThatThrownBy(() -> new InviteCode(shortCode))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(LENGTH + "글자");
    }
}
