package econovation.moongtaengi.study.api.dto;

import econovation.moongtaengi.study.application.comment.UpdateCommentCommand;
import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String content
) {
    public UpdateCommentCommand toCommand(Long commentId, Long memberId) {
        return new UpdateCommentCommand(commentId, memberId, content);
    }
}
