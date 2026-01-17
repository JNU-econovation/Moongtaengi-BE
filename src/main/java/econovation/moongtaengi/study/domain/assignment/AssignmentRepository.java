package econovation.moongtaengi.study.domain.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    boolean existsByProcessIdAndAssigneeId(Long processId, Long assigneeId);
}
