package econovation.moongtaengi.study.application.reaction;

import econovation.moongtaengi.study.domain.reaction.EmojiType;
import econovation.moongtaengi.study.domain.reaction.Reaction;
import econovation.moongtaengi.study.domain.reaction.ReactionMemberValidator;
import econovation.moongtaengi.study.domain.reaction.ReactionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ToggleReactionService {

    private final ReactionRepository reactionRepository;
    private final ReactionMemberValidator reactionMemberValidator;

    @Transactional
    public void toggleReaction(Long submissionId, Long memberId, EmojiType emojiType) {
        reactionMemberValidator.validate(memberId, submissionId);

        Optional<Reaction> reaction = reactionRepository
                .findBySubmissionIdAndMemberIdAndEmojiType(submissionId, memberId, emojiType);

        if (reaction.isPresent()) {
            reactionRepository.delete(reaction.get());

            log.info("감정표현 취소 성공 - submissionId: {}, memberId: {}, type: {}",
                    submissionId, memberId, emojiType);
        } else {
            Reaction newReaction = Reaction.create(submissionId, memberId, emojiType);
            reactionRepository.save(newReaction);

            log.info("감정표현 추가 성공 - submissionId: {}, memberId: {}, type: {}",
                    submissionId, memberId, emojiType);
        }
    }
}
