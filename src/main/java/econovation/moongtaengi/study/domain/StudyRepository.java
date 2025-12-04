package econovation.moongtaengi.study.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends JpaRepository<Study, Long> {
    @Query("select count(s) from Study s join s.members m where m.memberId = :memberId and m.role = :role")
    int countByMemberIdAndRole(@Param("memberId") Long memberId, @Param("role") StudyRole role);

    boolean existsByInviteCodeValue(String value);
}