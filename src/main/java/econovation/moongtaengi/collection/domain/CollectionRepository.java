package econovation.moongtaengi.collection.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 컬렉션 Repository
 */
@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {

    /**
     * 회원의 모든 컬렉션 조회
     * @param memberId 회원 ID
     * @return 컬렉션 목록
     */
    List<Collection> findAllByMemberId(Long memberId);

    /**
     * 회원의 특정 타입 컬렉션 조회
     * @param memberId 회원 ID
     * @param type 컬렉션 타입
     * @return 컬렉션 Optional
     */
    Optional<Collection> findByMemberIdAndType(Long memberId, CollectionType type);

    /**
     * 회원의 컬렉션 개수 조회
     * @param memberId 회원 ID
     * @return 컬렉션 개수
     */
    long countByMemberId(Long memberId);

    /**
     * 회원이 특정 타입 컬렉션을 보유하고 있는지 확인
     * @param memberId 회원 ID
     * @param type 컬렉션 타입
     * @return 보유 여부
     */
    boolean existsByMemberIdAndType(Long memberId, CollectionType type);
}