package econovation.moongtaengi.study.domain.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    boolean existsByProcessIdAndAssigneeId(Long processId, Long assigneeId);

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
}
