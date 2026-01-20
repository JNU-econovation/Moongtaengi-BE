package econovation.moongtaengi.study.domain.reaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ReactionTest {
    @Test
    @DisplayName("필수 정보(submissionId, memberId, emojiType)가 있으면 생성에 성공한다.")
    void 감정표현_생성_성공() {
        //given
        Long submissionId = 1L;
        Long userId = 100L;
        EmojiType type = EmojiType.HEART;

        //when
        Reaction reaction = Reaction.create(submissionId, userId, type);

        //then
        assertThat(reaction.getSubmissionId()).isEqualTo(submissionId);
        assertThat(reaction.getMemberId()).isEqualTo(userId);
        assertThat(reaction.getEmojiType()).isEqualTo(type);
    }

    @ParameterizedTest(name = "[{index}] {0}이 누락된 경우 예외 발생")
    @MethodSource("provideInvalidCreationArguments")
    @DisplayName("필수 정보(submissionId, userId, emojiType)가 누락되면 생성에 실패한다.")
    void 감정표현_필수_정보_누락_실패(String expectedMessage, Long submissionId, Long userId, EmojiType emojiType) {
        //when&then
        assertThatThrownBy(() -> Reaction.create(submissionId, userId, emojiType))
                .isInstanceOf(ReactionException.class)
                .extracting("errorCode")
                .isEqualTo(ReactionErrorCode.INVALID_REACTION_INFO);
    }

    private static Stream<Arguments> provideInvalidCreationArguments() {
        return Stream.of(
                Arguments.of("submissionId", null, 100L, EmojiType.HEART),
                Arguments.of("memberId", 1L, null, EmojiType.HEART),
                Arguments.of("emojiType", 1L, 100L, null)
        );
    }
}
