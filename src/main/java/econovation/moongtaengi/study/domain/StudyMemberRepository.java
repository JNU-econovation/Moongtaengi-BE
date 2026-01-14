package econovation.moongtaengi.study.domain;

import java.util.List;
import java.util.Optional;
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
}
