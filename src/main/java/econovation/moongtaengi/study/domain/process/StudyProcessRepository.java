package econovation.moongtaengi.study.domain.process;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyProcessRepository extends JpaRepository<StudyProcess, Long> {

    /**
     * 스터디 ID로 프로세스 목록 조회 (순서대로)
     */
    List<StudyProcess> findByStudyIdOrderByProcessOrder(Long studyId);

    /**
     * 스터디 ID로 프로세스 목록 조회 (시작일 기준 정렬)
     * processOrder 재정렬용
     */
    List<StudyProcess> findByStudyIdOrderByPeriodStartDate(Long studyId);

    /**
     * 스터디 ID로 프로세스 존재 여부
     */
    boolean existsByStudyId(Long studyId);

    /**
     * 스터디 ID로 프로세스 전체 삭제
     */
    void deleteByStudyId(Long studyId);

    @Query("""
        SELECT new econovation.moongtaengi.study.domain.process.StudyProcessPeriodBound(
            MIN(sp.period.startDate),
            MAX(sp.period.endDate)
        )
        FROM StudyProcess sp
        WHERE sp.studyId = :studyId
    """)
    StudyProcessPeriodBound findProcessPeriodBound(@Param("studyId") Long studyId);

    @Query("SELECT p.studyId FROM StudyProcess p WHERE p.id = :processId")
    Optional<Long> findStudyIdById(@Param("processId") Long processId);
}
