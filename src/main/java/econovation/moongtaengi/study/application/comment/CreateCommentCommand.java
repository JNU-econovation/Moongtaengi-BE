package econovation.moongtaengi.study.application.comment;

public record CreateCommentCommand(
        Long memberId,
        Long submissionId,
        String content
) {
}
