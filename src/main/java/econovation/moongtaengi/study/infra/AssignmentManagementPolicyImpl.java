package econovation.moongtaengi.study.infra;

import econovation.moongtaengi.study.domain.StudyMemberRepository;
import econovation.moongtaengi.study.domain.StudyRole;
import econovation.moongtaengi.study.domain.assignment.AssignmentErrorCode;
import econovation.moongtaengi.study.domain.assignment.AssignmentException;
import econovation.moongtaengi.study.domain.assignment.AssignmentManagementPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignmentManagementPolicyImpl implements AssignmentManagementPolicy {

    private final StudyMemberRepository studyMemberRepository;

    @Override
    public void validate(Long studyId, Long requesterId) {
        boolean isHost = studyMemberRepository.existsByStudyIdAndMemberIdAndRole(
                studyId, requesterId, StudyRole.HOST
        );

        if (!isHost) {
            throw new AssignmentException(AssignmentErrorCode.NO_MANAGEMENT_PERMISSION);
        }
    }
}
