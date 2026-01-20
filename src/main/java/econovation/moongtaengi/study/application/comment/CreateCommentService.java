package econovation.moongtaengi.study.application.comment;

import econovation.moongtaengi.study.domain.comment.Comment;
import econovation.moongtaengi.study.domain.comment.CommentContent;
import econovation.moongtaengi.study.domain.comment.CommentMemberValidator;
import econovation.moongtaengi.study.domain.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCommentService {

    private final CommentRepository commentRepository;
    private final CommentMemberValidator commentMemberValidator;

    public Long createComment(CreateCommentCommand command) {
        commentMemberValidator.validate(command.memberId(), command.submissionId());

        Comment comment = Comment.create(
                command.submissionId(),
                command.memberId(),
                new CommentContent(command.content())
        );

        commentRepository.save(comment);

        log.info("댓글 생성 성공 - commentId: {}, memberId: {}, submissionId: {}",
                comment.getId(), command.memberId(), command.submissionId());

        return comment.getId();
    }

}
