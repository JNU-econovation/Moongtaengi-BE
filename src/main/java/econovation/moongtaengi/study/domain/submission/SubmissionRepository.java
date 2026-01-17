package econovation.moongtaengi.study.domain.submission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    boolean existsByAssignmentId(Long assignmentId);

}
