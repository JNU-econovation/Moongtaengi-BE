package econovation.moongtaengi.study.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    @Query("select sm from StudyMember sm join fetch sm.study where sm.memberId = :memberId and sm.role = :role")
    List<StudyMember> findAllByMemberIdAndRole(@Param("memberId") Long memberId, @Param("role") StudyRole role);

    @Query("select sm from StudyMember sm join fetch sm.study where sm.study.id = :studyId and sm.memberId = :memberId")
    Optional<StudyMember> findByStudyIdAndMemberId(@Param("studyId") Long studyId, @Param("memberId") Long memberId);

    @Query("select count(sm) > 0 from StudyMember sm where sm.study.id = :studyId and sm.memberId = :memberId")
    boolean existsByStudyIdAndMemberId(@Param("studyId") Long studyId, @Param("memberId") Long memberId);

    @Query("select sm from StudyMember sm where sm.memberId = :memberId")
    List<StudyMember> findAllByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("delete from StudyMember sm where sm.memberId = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

    @Query("select sm from StudyMember sm where sm.study.id = :studyId")
    List<StudyMember> findAllByStudyId(@Param("studyId") Long studyId);

    @Query("select count(sm) > 0 from StudyMember sm " +
            "where sm.study.id = :studyId " +
            "and sm.memberId = :memberId " +
            "and sm.role = :role")
    boolean existsByStudyIdAndMemberIdAndRole(
            @Param("studyId") Long studyId,
            @Param("memberId") Long memberId,
            @Param("role") StudyRole role
    );

    /**
     * 회원이 참여 중인 스터디 중 가장 최근에 업데이트된 스터디 조회
     * @param memberId 회원 ID
     * @param pageable 페이징 정보 (Limit 1)
     * @return 가장 최근 업데이트된 스터디
     */
    @Query("SELECT sm FROM StudyMember sm JOIN FETCH sm.study s WHERE sm.memberId = :memberId ORDER BY s.updatedAt DESC")
    List<StudyMember> findMostRecentStudyByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
