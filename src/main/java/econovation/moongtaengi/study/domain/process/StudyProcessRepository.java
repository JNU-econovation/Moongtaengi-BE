package econovation.moongtaengi.study.domain.process;

import org.springframework.data.jpa.repository.JpaRepository;
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
    List<StudyProcess> findByStudyIdOrderByStartDate(Long studyId);

    /**
     * 스터디 ID로 프로세스 존재 여부
     */
    boolean existsByStudyId(Long studyId);

    /**
     * 스터디 ID로 프로세스 전체 삭제
     */
    void deleteByStudyId(Long studyId);
}
