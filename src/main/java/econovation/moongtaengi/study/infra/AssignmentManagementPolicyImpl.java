package econovation.moongtaengi.study.infra;

import econovation.moongtaengi.study.domain.StudyException;
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
    public void validateAuthority(Long studyId, Long requesterId) {
        // 1. 방장(관리자) 권한 검증 - 이것만 단독으로 쓰일 때도 있고, 할당 때 재사용되기도 함
        boolean isHost = studyMemberRepository.existsByStudyIdAndMemberIdAndRole(
                studyId, requesterId, StudyRole.HOST
        );

        if (!isHost) {
            throw new AssignmentException(AssignmentErrorCode.NO_MANAGEMENT_PERMISSION);
        }
    }

    @Override
    public void validateAllocation(Long studyId, Long requesterId, Long assigneeId) {
        validateAuthority(studyId, requesterId);

        boolean isAssigneeMember = studyMemberRepository.existsByStudyIdAndMemberId(
                studyId, assigneeId
        );

        if (!isAssigneeMember) {
            throw new AssignmentException(AssignmentErrorCode.INVALID_ASSIGNEE);
        }
    }
}
