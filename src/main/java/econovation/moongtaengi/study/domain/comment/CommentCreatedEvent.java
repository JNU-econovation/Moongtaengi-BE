package econovation.moongtaengi.study.domain.comment;

/**
 * 댓글 생성 이벤트
 */
public record CommentCreatedEvent(
        Long commentId,
        Long submissionId,
        Long commenterId
) {
}