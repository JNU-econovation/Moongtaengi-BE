package econovation.moongtaengi.study.application.comment;

import econovation.moongtaengi.study.domain.comment.Comment;
import econovation.moongtaengi.study.domain.comment.CommentContent;
import econovation.moongtaengi.study.domain.comment.CommentCreatedEvent;
import econovation.moongtaengi.study.domain.comment.CommentMemberValidator;
import econovation.moongtaengi.study.domain.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCommentService {

    private final CommentRepository commentRepository;
    private final CommentMemberValidator commentMemberValidator;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long createComment(CreateCommentCommand command) {
        commentMemberValidator.validate(command.memberId(), command.submissionId());

        Comment comment = Comment.create(
                command.submissionId(),
                command.memberId(),
                new CommentContent(command.content())
        );

        commentRepository.save(comment);

        eventPublisher.publishEvent(new CommentCreatedEvent(
                comment.getId(),
                comment.getSubmissionId(),
                comment.getMemberId()
                )
        );

        log.info("댓글 생성 성공 - commentId: {}, memberId: {}, submissionId: {}",
                comment.getId(), command.memberId(), command.submissionId());

        return comment.getId();
    }

}
