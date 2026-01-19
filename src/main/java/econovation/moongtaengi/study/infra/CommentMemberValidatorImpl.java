package econovation.moongtaengi.study.infra;

import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.comment.CommentErrorCode;
import econovation.moongtaengi.study.domain.comment.CommentException;
import econovation.moongtaengi.study.domain.comment.CommentMemberValidator;
import econovation.moongtaengi.study.domain.submission.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CommentMemberValidatorImpl implements CommentMemberValidator {

    private final SubmissionRepository submissionRepository;
    private final StudyMemberRepository studyMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public void validate(Long memberId, Long submissionId) {
        Long studyId = submissionRepository.findStudyIdById(submissionId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        boolean isStudyMember = studyMemberRepository.existsByStudyIdAndMemberId(studyId, memberId);

        if (!isStudyMember) {
            throw new CommentException(CommentErrorCode.NO_PERMISSION);
        }
    }
}
