package econovation.moongtaengi.member.infra;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BadWordFilteringImplTest {
    private final BadWordFilteringImpl validator = new BadWordFilteringImpl();

    @Test
    @DisplayName("비속어가 포함되어 있으면 true를 반환한다.")
    void 욕설_포함_테스트() {
        String text = "개똥";

        boolean result = validator.containsBadWord(text);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("비속어가 없으면 false를 반환한다")
    void 정상_단어_테스트() {
        String text = "뭉탱이";

        boolean result = validator.containsBadWord(text);

        assertThat(result).isFalse();
    }
}
