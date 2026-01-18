package econovation.moongtaengi.study.api.dto;

import econovation.moongtaengi.study.application.comment.CreateCommentCommand;

public record CommentCreateRequest(
        String content
) {
    public CreateCommentCommand toCommand(Long memberId, Long submissionId) {
        return new CreateCommentCommand(
                memberId,
                submissionId,
                content
        );
    }
}
