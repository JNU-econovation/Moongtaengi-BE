package econovation.moongtaengi.study.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    @Query("select sm from StudyMember sm join fetch sm.study where sm.memberId = :memberId and sm.role = :role")
    List<StudyMember> findAllByMemberIdAndRole(@Param("memberId") Long memberId, @Param("role") StudyRole role);
}
