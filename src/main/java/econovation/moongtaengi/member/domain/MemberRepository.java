package econovation.moongtaengi.member.domain;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByKakaoId(String kakaoId);

    boolean existsByKakaoId(String kakaoId);

    boolean existsByNicknameValue(String nickname);

    List<Member> findAllByStatus(MemberStatus status);

    long countByStatus(MemberStatus status);

    /**
     * 경험치 상위 회원 조회
     * @param pageable 페이징 정보 (Limit 5)
     * @return 경험치 상위 회원 목록
     */
    @Query("SELECT m FROM Member m WHERE m.status = 'ACTIVE' ORDER BY m.totalExperience DESC")
    List<Member> findTopMembersByExperience(Pageable pageable);
}
