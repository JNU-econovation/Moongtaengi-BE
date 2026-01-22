package econovation.moongtaengi.study.application.comment;

import econovation.moongtaengi.study.domain.comment.CommentMemberValidator;
import econovation.moongtaengi.study.domain.comment.CommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {

    private final CommentRepository commentRepository;
    private final CommentMemberValidator commentMemberValidator;

    public List<CommentSummary> getComments(Long submissionId, Long loginMemberId) {
        commentMemberValidator.validate(loginMemberId, submissionId);

        return commentRepository.findRawListBySubmissionId(submissionId).stream()
                .map(raw -> CommentSummary.of(loginMemberId, raw))
                .toList();
    }
}
