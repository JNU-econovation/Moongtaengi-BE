package econovation.moongtaengi.study.domain.submission;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    boolean existsByAssignmentId(Long assignmentId);

    @Query("select p.studyId " +
            "from Submission s " +
            "join Assignment a on s.assignmentId = a.id " +
            "join StudyProcess p on a.processId = p.id " +
            "where s.id = :submissionId")
    Optional<Long> findStudyIdById(@Param("submissionId") Long submissionId);

}
