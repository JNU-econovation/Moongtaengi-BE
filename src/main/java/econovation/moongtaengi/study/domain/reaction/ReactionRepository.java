package econovation.moongtaengi.study.domain.reaction;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findBySubmissionIdAndMemberIdAndEmojiType(Long submissionId, Long memberId, EmojiType emojiType);
}
