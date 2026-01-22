package econovation.moongtaengi.study.domain.reaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ReactionRepositoryTest {

    @Autowired
    private ReactionRepository reactionRepository;

    @Test
    @DisplayName("특정 제출물의 리액션 통계(개수, 내 반응 여부)를 정확히 조회한다")
    void 리액션_통계_조회_성공() {
        //given
        Long submissionId = 100L;
        Long myId = 1L;
        Long otherId = 2L;

        reactionRepository.save(Reaction.create(submissionId, myId, EmojiType.HEART));
        reactionRepository.save(Reaction.create(submissionId, otherId, EmojiType.HEART));
        reactionRepository.save(Reaction.create(submissionId, otherId, EmojiType.CLAP));

        reactionRepository.save(Reaction.create(200L, myId, EmojiType.HEART));

        //when
        List<ReactionStat> result = reactionRepository.findStatBySubmissionIdAndMemberId(submissionId, myId);

        //then
        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(ReactionStat::emojiType, ReactionStat::count, ReactionStat::isClicked)
                .containsExactlyInAnyOrder(
                        tuple(EmojiType.HEART, 2L, true),
                        tuple(EmojiType.CLAP, 1L, false)
                );
    }

    @Test
    @DisplayName("리액션이 하나도 없는 제출물 조회 시 빈 리스트를 반환한다")
    void 리액션_없음_빈리스트_반환() {
        //given
        Long submissionId = 100L;
        Long memberId = 1L;

        //when
        List<ReactionStat> result = reactionRepository.findStatBySubmissionIdAndMemberId(submissionId, memberId);

        //then
        assertThat(result).isEmpty();
    }
}