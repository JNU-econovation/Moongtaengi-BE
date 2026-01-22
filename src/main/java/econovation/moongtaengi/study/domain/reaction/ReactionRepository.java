package econovation.moongtaengi.study.domain.reaction;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findBySubmissionIdAndMemberIdAndEmojiType(Long submissionId, Long memberId, EmojiType emojiType);

    long countByMemberId(Long memberId);

    @Query("""
        SELECT new econovation.moongtaengi.study.domain.reaction.ReactionStat(
            r.emojiType,
            COUNT(r),
            SUM(CASE WHEN r.memberId = :memberId THEN 1L ELSE 0L END)
        )
        FROM Reaction r
        WHERE r.submissionId = :submissionId
        GROUP BY r.emojiType
    """)
    List<ReactionStat> findStatBySubmissionIdAndMemberId(
            @Param("submissionId") Long submissionId,
            @Param("memberId") Long memberId
    );
}
