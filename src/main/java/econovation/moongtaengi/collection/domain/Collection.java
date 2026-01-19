package econovation.moongtaengi.collection.domain;

import econovation.moongtaengi.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 컬렉션 엔티티
 * 회원이 해금한 뭉탱이 아이콘을 관리
 * 이미지 URL은 CollectionType Enum에서 관리
 */
@Entity
@Table(name = "collections")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Collection extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CollectionType type;

    @Column(nullable = false)
    private LocalDateTime unlockedAt;

    @Builder
    private Collection(Long memberId, CollectionType type, LocalDateTime unlockedAt) {
        this.memberId = memberId;
        this.type = type;
        this.unlockedAt = unlockedAt;
    }

    /**
     * 컬렉션 생성 (해금)
     * @param memberId 회원 ID
     * @param type 컬렉션 타입
     * @return 생성된 컬렉션
     */
    public static Collection unlock(Long memberId, CollectionType type) {
        return Collection.builder()
                .memberId(memberId)
                .type(type)
                .unlockedAt(LocalDateTime.now())
                .build();
    }
}