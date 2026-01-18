package econovation.moongtaengi.study.domain.assignment;

import econovation.moongtaengi.study.application.assignment.AssignmentSummary;
import java.util.List;
import java.util.Optional;
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
            a.isLate,
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
           
    /**
     * 특정 프로세스의 특정 상태인 과제 조회 (알림 스케줄러 최적화)
     */
    @Query("SELECT a FROM Assignment a WHERE a.processId = :processId AND a.status = :status")
    List<Assignment> findByProcessIdAndStatus(@Param("processId") Long processId, @Param("status") AssignmentStatus status);

    /**
     * 여러 프로세스의 특정 상태인 과제 조회 (알림 스케줄러 최적화)
     */
    @Query("SELECT a FROM Assignment a WHERE a.processId IN :processIds AND a.status = :status")
    List<Assignment> findByProcessIdInAndStatus(@Param("processIds") List<Long> processIds, @Param("status") AssignmentStatus status);

    @Query("""
        SELECT new econovation.moongtaengi.study.domain.assignment.AssignmentDetailRaw(
            s.name.value,
            a.content.value,
            m.nickname.value,
            a.assigneeId,
            m.totalExperience,
            sub.id,
            sub.createdAt
        )
        FROM Assignment a
        JOIN StudyProcess p ON a.processId = p.id
        JOIN Study s ON p.studyId = s.id
        JOIN Member m ON m.id = a.assigneeId
        LEFT JOIN Submission sub ON sub.assignmentId = a.id AND sub.submitterId = m.id
        WHERE a.id = :assignmentId
    """)
    Optional<AssignmentDetailRaw> findDetailRawById(@Param("assignmentId") Long assignmentId);
}
