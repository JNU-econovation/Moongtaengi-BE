package econovation.moongtaengi.study.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import econovation.moongtaengi.study.application.reaction.ToggleReactionService;
import econovation.moongtaengi.study.domain.reaction.EmojiType;
import econovation.moongtaengi.study.domain.reaction.Reaction;
import econovation.moongtaengi.study.domain.reaction.ReactionMemberValidator;
import econovation.moongtaengi.study.domain.reaction.ReactionRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ToggleReactionServiceTest {
    @InjectMocks
    private ToggleReactionService toggleReactionService;

    @Mock
    private ReactionRepository reactionRepository;

    @Mock
    private ReactionMemberValidator reactionMemberValidator;

    @Captor
    private ArgumentCaptor<Reaction> reactionCaptor;

    @Test
    @DisplayName("기존에 감정표현이 없으면 권한 검사 후 저장한다.")
    void 기존_감정표현_없음_저장_성공() {
        //given
        Long submissionId = 1L;
        Long memberId = 100L;
        EmojiType type = EmojiType.HEART;

        given(reactionRepository.findBySubmissionIdAndMemberIdAndEmojiType(submissionId, memberId, type))
                .willReturn(Optional.empty());

        //when
        toggleReactionService.toggleReaction(submissionId, memberId, type);

        //then
        verify(reactionMemberValidator).validate(memberId, submissionId);
        verify(reactionRepository).save(reactionCaptor.capture());

        Reaction savedReaction = reactionCaptor.getValue();
        assertThat(savedReaction.getSubmissionId()).isEqualTo(submissionId);
        assertThat(savedReaction.getMemberId()).isEqualTo(memberId);
        assertThat(savedReaction.getEmojiType()).isEqualTo(type);

        verify(reactionRepository, never()).delete(any(Reaction.class));
    }

    @Test
    @DisplayName("기존에 감정표현이 있으면 권한 검사 후 삭제한다.")
    void 기존_감정표현_있음() {
        //given
        Long submissionId = 1L;
        Long memberId = 100L;
        EmojiType type = EmojiType.HEART;

        Reaction existingReaction = Reaction.create(submissionId, memberId, type);

        given(reactionRepository.findBySubmissionIdAndMemberIdAndEmojiType(submissionId, memberId, type))
                .willReturn(Optional.of(existingReaction));

        //when
        toggleReactionService.toggleReaction(submissionId, memberId, type);

        //then
        verify(reactionMemberValidator).validate(memberId, submissionId);
        verify(reactionRepository).delete(existingReaction);
        verify(reactionRepository, never()).save(any(Reaction.class));
    }
}
