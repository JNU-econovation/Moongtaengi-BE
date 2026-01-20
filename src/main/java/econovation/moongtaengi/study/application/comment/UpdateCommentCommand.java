package econovation.moongtaengi.study.application.comment;

public record UpdateCommentCommand(
        Long commentId,
        Long memberId,
        String content
) {
}
