package econovation.moongtaengi.study.domain.assignment;

public interface AssignmentManagementPolicy {
    void validate(Long studyId, Long requesterId);
}
