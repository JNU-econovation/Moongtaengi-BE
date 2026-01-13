package econovation.moongtaengi.member.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByKakaoId(String kakaoId);

    boolean existsByKakaoId(String kakaoId);

    boolean existsByNicknameValue(String nickname);

    List<Member> findAllByStatus(MemberStatus status);

    long countByStatus(MemberStatus status);
}
