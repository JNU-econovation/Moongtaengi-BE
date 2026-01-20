package econovation.moongtaengi.study.api.dto;

import econovation.moongtaengi.study.application.comment.CreateCommentCommand;
import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
        @NotBlank(message = "댓글 내용은 필수입니다.")
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
