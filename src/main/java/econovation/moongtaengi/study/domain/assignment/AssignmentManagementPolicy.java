package econovation.moongtaengi.study.domain.assignment;

public interface AssignmentManagementPolicy {
    void validateAllocation(Long studyId, Long requesterId, Long assigneeId);

    void validateAuthority(Long studyId, Long requesterId);
}
