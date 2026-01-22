package econovation.moongtaengi.study.domain.comment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    long countByMemberId(Long memberId);

    @Query("""
        SELECT new econovation.moongtaengi.study.domain.comment.CommentSummaryRaw(
            c.id,
            c.content.value,
            c.createdAt,
            m.id,
            m.nickname.value,
            m.profileIcon
        )
        FROM Comment c
        JOIN Member m ON c.memberId = m.id
        WHERE c.submissionId = :submissionId
        ORDER BY c.createdAt ASC
    """)
    List<CommentSummaryRaw> findRawListBySubmissionId(@Param("submissionId") Long submissionId);
}
