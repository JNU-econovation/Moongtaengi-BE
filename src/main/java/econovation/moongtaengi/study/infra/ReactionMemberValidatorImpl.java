package econovation.moongtaengi.study.infra;

import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.reaction.ReactionErrorCode;
import econovation.moongtaengi.study.domain.reaction.ReactionException;
import econovation.moongtaengi.study.domain.reaction.ReactionMemberValidator;
import econovation.moongtaengi.study.domain.submission.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReactionMemberValidatorImpl implements ReactionMemberValidator {

    private final SubmissionRepository submissionRepository;
    private final StudyMemberRepository studyMemberRepository;

    @Override
    public void validate(Long memberId, Long submissionId) {
        Long studyId = submissionRepository.findStudyIdById(submissionId)
                .orElseThrow(() -> new ReactionException(ReactionErrorCode.SUBMISSION_NOT_FOUND));

        boolean isStudyMember = studyMemberRepository.existsByStudyIdAndMemberId(studyId, memberId);

        if (!isStudyMember) {
            throw new ReactionException(ReactionErrorCode.NO_PERMISSION);
        }
    }
}
