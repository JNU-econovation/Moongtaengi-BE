package econovation.moongtaengi.study.domain.assignment;

import econovation.moongtaengi.study.application.assignment.AssignmentSummary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    boolean existsByProcessIdAndAssigneeId(Long processId, Long assigneeId);

    @Query("""
        SELECT new econovation.moongtaengi.study.application.assignment.AssignmentSummary(
            a.id,
            s.id,
            m.id,
            a.content.value,
            m.nickname.value,
            a.status,
            sa.url
        )
        FROM StudyMember sm
        JOIN Member m ON sm.memberId = m.id
        LEFT JOIN Assignment a ON a.assigneeId = m.id AND a.processId = :processId
        LEFT JOIN Submission s ON s.assignmentId = a.id AND s.submitterId = m.id
        LEFT JOIN s.attachments sa
        WHERE sm.study.id = :studyId
    """)
    List<AssignmentSummary> findSummaryByProcessIdAndStudyId(
            @Param("processId") Long processId,
            @Param("studyId") Long studyId
    );
}
