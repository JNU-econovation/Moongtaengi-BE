package econovation.moongtaengi.study.application.comment;

import econovation.moongtaengi.study.domain.comment.Comment;
import econovation.moongtaengi.study.domain.comment.CommentContent;
import econovation.moongtaengi.study.domain.comment.CommentErrorCode;
import econovation.moongtaengi.study.domain.comment.CommentException;
import econovation.moongtaengi.study.domain.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateCommentService {

    private final CommentRepository commentRepository;

    public void updateComment(UpdateCommentCommand command) {
        Comment comment = commentRepository.findById(command.commentId())
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        CommentContent content = new CommentContent(command.content());

        comment.updateContent(command.memberId(), content);

        log.info("댓글 수정 성공 - commentId: {}, memberId: {}", command.commentId(), command.memberId());
    }
}
